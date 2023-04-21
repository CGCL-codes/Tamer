    public static String purgeHistory(int history) throws Exception {
        log.debug("Purging log messages");
        Configuration config = new Configuration();
        String STAGINGDIR = config.getProperty("stagingDirectory");
        int PurgedLog = 0;
        int PurgedAudit = 0;
        int PurgedStage = 0;
        int PurgedStageFile = 0;
        Calendar cal = new GregorianCalendar();
        cal.add(Calendar.DATE, -history);
        java.util.Date historyDate = cal.getTime();
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        Query q = session.createQuery("SELECT a.messageid FROM Audit as a " + "WHERE a.time <= :historyDate ").setDate("historyDate", historyDate);
        Iterator messageidIterator = q.list().iterator();
        String purgeLog = "";
        while (messageidIterator.hasNext()) {
            String purgeMessage = (String) messageidIterator.next();
            PurgedLog += session.createQuery("delete TxnLog as l " + "WHERE l.messageid = :messageid").setString("messageid", purgeMessage).executeUpdate();
            PurgedAudit += session.createQuery("delete Audit as a " + "WHERE a.messageid = :messageid").setString("messageid", purgeMessage).executeUpdate();
            Query stageQ = session.createQuery("SELECT s.fileid FROM Stage as s " + "WHERE s.messageid = :messageid ").setString("messageid", purgeMessage);
            Iterator stageIterator = stageQ.list().iterator();
            while (stageIterator.hasNext()) {
                Integer stageFileID = (Integer) stageIterator.next();
                File stageFile = new File(STAGINGDIR + "/" + stageFileID);
                try {
                    stageFile.delete();
                    PurgedStageFile++;
                } catch (Exception rmError) {
                    log.error("--Could not purge stage file: " + STAGINGDIR + "/" + stageFileID);
                    purgeLog += "--ERROR: Could not purge stage file: " + STAGINGDIR + "/" + stageFileID;
                }
            }
            PurgedStage += session.createQuery("delete Stage as s " + "WHERE s.messageid = :messageid").setString("messageid", purgeMessage).executeUpdate();
            session.flush();
            session.clear();
        }
        try {
            tx.commit();
            log.info("-Purged \"" + PurgedAudit + "\" audit logs.");
            purgeLog += "-Purged \"" + PurgedAudit + "\" audit logs.\n";
            log.info("-Purged \"" + PurgedLog + "\" transaction logs.");
            purgeLog += "-Purged \"" + PurgedLog + "\" transaction logs.\n";
            log.info("-Purged \"" + PurgedStage + "\" stage logs.");
            purgeLog += "-Purged \"" + PurgedStage + "\" stage logs.\n";
            log.info("-Purged \"" + PurgedStageFile + "\" stage files.");
            purgeLog += "-Purged \"" + PurgedStageFile + "\" stage files.";
        } catch (Exception e) {
            log.debug("Rolled Back Transaction: " + e);
            tx.rollback();
            throw new Exception(e);
        }
        return purgeLog;
    }
