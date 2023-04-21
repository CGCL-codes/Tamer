    public void migrateDatabaseTo(EntityManager em, String schemaName, int version) throws IOException {
        int localversion = getLocalVersion();
        if (version > localversion) {
            throw new IllegalArgumentException("Version is not yet implemented: " + version);
        }
        int databaseVersion = getDatabaseVersion(em, schemaName);
        if (databaseVersion == version) {
            return;
        }
        boolean direction = databaseVersion < version;
        int start = direction ? databaseVersion + 1 : databaseVersion;
        int end = direction ? version + 1 : version;
        int increment = direction ? +1 : -1;
        em.getTransaction().begin();
        try {
            for (int v = start; v != end; v = v + increment) {
                String[] scripts = readSQLScript(schemaName, v, direction);
                for (int i = 0; i < scripts.length; i++) {
                    String script = scripts[i];
                    Query query = em.createNativeQuery(script);
                    query.executeUpdate();
                }
            }
            if (version != -1) {
                String versionUpdateSql = UPDATE_VERSION.replace("${schema_name}", schemaName).replace("${version}", String.valueOf(version));
                Query query = em.createNativeQuery(versionUpdateSql);
                query.executeUpdate();
            }
            em.getTransaction().commit();
        } catch (RuntimeException rex) {
            rex.printStackTrace();
            em.getTransaction().rollback();
            throw rex;
        }
    }
