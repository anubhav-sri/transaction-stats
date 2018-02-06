import com.transaction.exceptions.TransactionExpiredException;
import com.transaction.models.Transaction;
import com.transaction.models.TransactionStats;
import com.transaction.persistence.TransactionDatabase;
import com.transaction.services.TransactionsService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.rules.ExpectedException.none;

@RunWith(MockitoJUnitRunner.class)
public class TransactionsServiceTest {

    @Rule
    public final ExpectedException exceptionThrown = none();
    private TransactionsService transactionsService;
    private Clock clock = Clock.systemUTC();

    private TransactionDatabase transactionDatabase;

    @Before
    public void setUp() throws Exception {
        transactionDatabase = new TransactionDatabase();
        transactionsService = new TransactionsService(transactionDatabase, clock);
    }

    @Test
    public void shouldSaveTheTransaction() throws TransactionExpiredException {
        double amount = 123;
        long transactionTime = Instant.now(Clock.systemUTC()).toEpochMilli();
        Transaction validTransaction = new Transaction(amount, transactionTime);

        transactionsService.saveTransaction(validTransaction);
        assertThat(transactionDatabase.get(transactionTime)).isEqualTo(validTransaction);
    }

    @Test
    public void shouldConsiderLast60SecTransactionsInStats() throws TransactionExpiredException {
        double amount = 123;
        long transactionTime = Instant.now(Clock.systemUTC()).toEpochMilli();
        long staleTransactionTime = Instant.now(Clock.systemUTC()).minusSeconds(61).toEpochMilli();
        Transaction validTransaction = new Transaction(amount, transactionTime);
        Transaction staleTransaction = new Transaction(amount, staleTransactionTime);
        transactionDatabase.put(transactionTime, validTransaction);
        transactionDatabase.put(transactionTime, staleTransaction);

        TransactionStats transactionStats = new TransactionStats(123, 123, 123, 1);

        TransactionStats currentStats = transactionsService.getCurrentStats();
        assertThat(currentStats).isEqualTo(transactionStats);
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