import com.transaction.exceptions.TransactionExpiredException;
import com.transaction.models.Transaction;
import com.transaction.models.TransactionStats;
import com.transaction.persistence.TransactionDatabase;
import com.transaction.services.TransactionsService;
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

public class TransactionsServiceTest {

    @Rule
    public final ExpectedException exceptionThrown = none();
    private TransactionsService transactionsService;
    private Clock clock = Clock.systemUTC();

    @Before
    public void setUp() throws Exception {
        transactionsService = new TransactionsService(new TransactionDatabase(), clock);
    }

    @Test
    public void shouldBeAbleToSaveTheUpdateTheTransactionHistory() throws TransactionExpiredException {
        double amount = 123;
        long transactionTime = Instant.now(Clock.systemUTC()).toEpochMilli();
        Transaction validTransaction = new Transaction(amount, transactionTime);

        transactionsService.saveTransaction(validTransaction);
        TransactionStats stats = transactionsService.getCurrentStats();

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

        transactionsService.saveTransaction(transaction);
    }

}