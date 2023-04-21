    public void init() {
        ThreadCategory.setPrefix(LOG4J_CATEGORY);
        Category log = ThreadCategory.getInstance(getClass());
        int numWriters = 1;
        try {
            OutageManagerConfigFactory.reload();
            OutageManagerConfigFactory oFactory = OutageManagerConfigFactory.getInstance();
            numWriters = oFactory.getWriters();
        } catch (MarshalException ex) {
            log.error("Failed to load outage configuration", ex);
            throw new UndeclaredThrowableException(ex);
        } catch (ValidationException ex) {
            log.error("Failed to load outage configuration", ex);
            throw new UndeclaredThrowableException(ex);
        } catch (IOException ex) {
            log.error("Failed to load outage configuration", ex);
            throw new UndeclaredThrowableException(ex);
        }
        java.sql.Connection conn = null;
        try {
            DatabaseConnectionFactory.init();
            conn = DatabaseConnectionFactory.getInstance().getConnection();
            closeOutages(conn);
            buildServiceTableMap(conn);
        } catch (IOException ie) {
            log.fatal("IOException getting database connection", ie);
            throw new UndeclaredThrowableException(ie);
        } catch (MarshalException me) {
            log.fatal("Marshall Exception getting database connection", me);
            throw new UndeclaredThrowableException(me);
        } catch (ValidationException ve) {
            log.fatal("Validation Exception getting database connection", ve);
            throw new UndeclaredThrowableException(ve);
        } catch (SQLException sqlE) {
            log.fatal("Error closing outages for unmanaged services and interfaces or building servicename to serviceid mapping", sqlE);
            throw new UndeclaredThrowableException(sqlE);
        } catch (ClassNotFoundException cnfE) {
            log.fatal("Failed to load database driver", cnfE);
            throw new UndeclaredThrowableException(cnfE);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e) {
                }
            }
        }
        m_writerPool = new RunnableConsumerThreadPool("Outage Writer Pool", 0.6f, 1.0f, numWriters);
        if (log.isDebugEnabled()) log.debug("Created writer pool");
        m_eventReceiver = new BroadcastEventProcessor(m_writerPool.getRunQueue());
        if (log.isDebugEnabled()) log.debug("Created event receiver");
        log.info("OutageManager ready to accept events");
    }
