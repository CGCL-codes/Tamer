    public static boolean startDatabaseProcedure(ProcessInfo processInfo, String ProcedureName, Trx trx) {
        String sql = "{call " + ProcedureName + "(?)}";
        String trxName = trx != null ? trx.getTrxName() : null;
        try {
            CallableStatement cstmt = DB.prepareCall(sql, ResultSet.CONCUR_UPDATABLE, trxName);
            cstmt.setInt(1, processInfo.getAD_PInstance_ID());
            cstmt.executeUpdate();
            cstmt.close();
            if (trx != null && trx.isActive()) {
                trx.commit(true);
                trx.close();
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, sql, e);
            if (trx != null && trx.isActive()) {
                trx.rollback();
                trx.close();
            }
            processInfo.setSummary(Msg.getMsg(Env.getCtx(), "ProcessRunError") + " " + e.getLocalizedMessage());
            processInfo.setError(true);
            return false;
        }
        return true;
    }
