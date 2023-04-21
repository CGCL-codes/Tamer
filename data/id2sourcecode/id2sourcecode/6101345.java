    private void createDB() {
        if (!serverThread.isStarted) {
            start();
        }
        try {
            InputStream sqlStream = getClass().getResourceAsStream("/createDB.sql");
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
            sf.execute();
            f.delete();
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage(), e);
        }
    }
