    public LargeObjectManager(BaseConnection conn) throws SQLException {
        this.conn = conn;
        this.fp = conn.getFastpathAPI();
        String sql;
        if (conn.getMetaData().supportsSchemasInTableDefinitions()) {
            sql = "SELECT p.proname,p.oid " + " FROM pg_catalog.pg_proc p, pg_catalog.pg_namespace n " + " WHERE p.pronamespace=n.oid AND n.nspname='pg_catalog' AND (";
        } else {
            sql = "SELECT proname,oid FROM pg_proc WHERE ";
        }
        sql += " proname = 'lo_open'" + " or proname = 'lo_close'" + " or proname = 'lo_creat'" + " or proname = 'lo_unlink'" + " or proname = 'lo_lseek'" + " or proname = 'lo_tell'" + " or proname = 'loread'" + " or proname = 'lowrite'" + " or proname = 'lo_truncate'";
        if (conn.getMetaData().supportsSchemasInTableDefinitions()) {
            sql += ")";
        }
        ResultSet res = conn.createStatement().executeQuery(sql);
        if (res == null) throw new PSQLException(GT.tr("Failed to initialize LargeObject API"), PSQLState.SYSTEM_ERROR);
        fp.addFunctions(res);
        res.close();
        conn.getLogger().debug("Large Object initialised");
    }
