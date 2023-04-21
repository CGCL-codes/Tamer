    public void execute(JobExecutionContext context) throws JobExecutionException {
        SessionFactory sessionFactory = (SessionFactory) context.getJobDetail().getJobDataMap().get(SchedulerJobModule.HIBERNATE_SESSION_SOURCE);
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Date date = new Date();
            long time = date.getTime();
            Query q = session.getNamedQuery(OnlineUser.DELETE_TIMEOUT_USERS_QUERY);
            q.setLong(0, time - MAX_ONLINE_TIME);
            int rows = q.executeUpdate();
            if (logger.isDebugEnabled()) {
                logger.debug("delete online users [" + rows + "]");
            }
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            transaction.rollback();
        } finally {
            if (session.isConnected()) {
                session.close();
            }
        }
    }
