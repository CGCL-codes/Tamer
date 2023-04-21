    public static TransactionManagerLookup getTransactionManagerLookup(Properties props) throws HibernateException {
        String tmLookupClass = props.getProperty(Environment.TRANSACTION_MANAGER_STRATEGY);
        if (tmLookupClass == null) {
            log.info("No TransactionManagerLookup configured (in JTA environment, use of read-write or transactional second-level cache is not recommended)");
            return null;
        } else {
            log.info("instantiating TransactionManagerLookup: " + tmLookupClass);
            try {
                TransactionManagerLookup lookup = (TransactionManagerLookup) ReflectHelper.classForName(tmLookupClass).newInstance();
                log.info("instantiated TransactionManagerLookup");
                return lookup;
            } catch (Exception e) {
                log.error("Could not instantiate TransactionManagerLookup", e);
                throw new HibernateException("Could not instantiate TransactionManagerLookup '" + tmLookupClass + "'");
            }
        }
    }
