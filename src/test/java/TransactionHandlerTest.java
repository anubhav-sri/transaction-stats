import com.transaction.handlers.Transaction;
import com.transaction.handlers.TransactionHandler;
import com.transaction.handlers.TransactionStats;
import com.transaction.handlers.exceptions.TransactionExpiredException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.rules.ExpectedException.none;

public class TransactionHandlerTest {

    @Rule
    public final ExpectedException exceptionThrown = none();
    private TransactionHandler transactionHandler;

    @Before
    public void setUp() throws Exception {
        transactionHandler = new TransactionHandler();
    }

    @Test
    public void shouldBeAbleToSaveTheUpdateTheTransactionHistory() throws TransactionExpiredException {
        double amount = 123;
        Transaction validTransaction = new Transaction(amount, LocalDateTime.now());

        transactionHandler.saveTransaction(validTransaction);
        TransactionStats stats = transactionHandler.getCurrentTransactionStats();

        assertThat(stats.getMax()).isEqualTo(amount);
        assertThat(stats.getSum()).isEqualTo(amount);
        assertThat(stats.getAvg()).isEqualTo(amount);
        assertThat(stats.getCount()).isEqualTo(1);
    }

    @Test
    public void shouldBeThrowExceptionIfTransactionIsExpired() throws TransactionExpiredException {
        LocalDateTime dateTime = LocalDateTime.now().minusSeconds(61);
        double amount = 123;
        Transaction transaction = new Transaction(amount, dateTime);
        exceptionThrown.expect(TransactionExpiredException.class);
        exceptionThrown.expectMessage("amount:123.0, time:" + dateTime.toEpochSecond(ZoneOffset.UTC));

        transactionHandler.saveTransaction(transaction);
    }

}