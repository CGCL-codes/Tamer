    private void dropDB() {
        if (!serverThread.isStarted) {
            start();
        }
        try {
            InputStream sqlStream = getClass().getResourceAsStream("/removeDB.sql");
            File f = File.createTempFile("sql" + System.currentTimeMillis(), ".sql");
            FileOutputStream fos = new FileOutputStream(f);
            byte[] data = new byte[1024];
            int read = -1;
            do {
                read = sqlStream.read(data);
                if (read > 0) {
                    fos.write(data, 0, read);
                }
            } while (read >= 0);
            SqlFile sf = new SqlFile(f);
            sf.setConnection(connection);
            Logger.getLogger(Accounting.class.getName()).log(Level.INFO, "Dropping all tables");
            sf.execute();
            f.delete();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage(), e);
        }
    }
