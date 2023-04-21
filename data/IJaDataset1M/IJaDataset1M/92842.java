package net.sf.kmoviecataloger.persistence;

/**
 *
 * @author sergiolopes
 */
public class PersistenceFactory {

    private static PersistenceFactory instance;

    private PersistenceFactory() {
    }

    public static PersistenceFactory getInstance() {
        if (PersistenceFactory.instance == null) {
            PersistenceFactory.instance = new PersistenceFactory();
        }
        return PersistenceFactory.instance;
    }

    public IPersistence createPersistenceEngine(String engine) {
        return new SQLEngine();
    }
}
