    protected Context start(PTask pt, String w, SyrupConnection con) throws Exception {
        PreparedStatement s = null;
        ResultSet result = null;
        try {
            s = con.prepareStatementFromCache(sqlImpl().sqlStatements().checkIsExecutableTaskStatement());
            s.setString(1, pt.key());
            result = s.executeQuery();
            if (result.next()) {
                java.util.Date dd = new java.util.Date();
                PreparedStatement s2 = null;
                s2 = con.prepareStatementFromCache(sqlImpl().sqlStatements().updateWorkerStatement());
                s2.setString(1, w);
                s2.setString(2, pt.key());
                s2.executeUpdate();
                sqlImpl().loggingFunctions().log(pt.key(), LogEntry.STARTED, con);
                Context c = sqlImpl().queryFunctions().readContext(pt, con);
                con.commit();
                return c;
            }
        } finally {
            con.rollback();
            sqlImpl().genericFunctions().close(result);
        }
        return null;
    }
