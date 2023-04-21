package au.gov.nla.aons.transaction;

/**
 * Exposes methods which allow transactions to be executed
 * around TransactionCallback objects.
 * 
 * @author dlevy
 *
 */
public class CallbackTransactionManagerImpl implements CallbackTransactionManager {

    public void performInNew(TransactionCallback callback) {
        callback.execute();
    }
}
