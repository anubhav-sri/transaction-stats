import com.transaction.exceptions.TransactionExpiredException;
import com.transaction.models.Transaction;
import com.transaction.models.TransactionStats;
import com.transaction.services.TransactionDatabase;
import com.transaction.services.TransactionStatsService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.rules.ExpectedException.none;

public class TransactionStatsServiceTest {

    @Rule
    public final ExpectedException exceptionThrown = none();
    private TransactionStatsService transactionStatsService;
    private Clock clock = Clock.systemUTC();

    @Before
    public void setUp() throws Exception {
        transactionStatsService = new TransactionStatsService(new TransactionDatabase(), clock);
    }

    @Test
    public void shouldBeAbleToSaveTheUpdateTheTransactionHistory() throws TransactionExpiredException {
        double amount = 123;
        long transactionTime = Instant.now(Clock.systemUTC()).toEpochMilli();
        Transaction validTransaction = new Transaction(amount, transactionTime);

        transactionStatsService.saveTransaction(validTransaction);
        TransactionStats stats = transactionStatsService.getCurrentStats();

        assertThat(stats.getMax()).isEqualTo(amount);
        assertThat(stats.getSum()).isEqualTo(amount);
        assertThat(stats.getAvg()).isEqualTo(amount);
        assertThat(stats.getCount()).isEqualTo(1);
    }

    @Test
    public void shouldBeThrowExceptionIfTransactionIsExpired() throws TransactionExpiredException {
        LocalDateTime dateTime = LocalDateTime.now(clock).minusSeconds(61);
        double amount = 123;
        Transaction transaction = new Transaction(amount, dateTime.toInstant(ZoneOffset.UTC).toEpochMilli());
        exceptionThrown.expect(TransactionExpiredException.class);
        exceptionThrown.expectMessage("amount:123.0, time:" + dateTime.toEpochSecond(ZoneOffset.UTC));

        transactionStatsService.saveTransaction(transaction);
    }

}