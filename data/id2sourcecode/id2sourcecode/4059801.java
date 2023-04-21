    public void createDB(File dir, String url) {
        try {
            dir.mkdirs();
            if (new File(dir, "db.conf").exists()) {
                Logger.log(Logger.DEBUG, "Database already exists.");
                return;
            }
            Logger.log(Logger.INFO, "Creating new database...");
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(dir, "db.conf")));
            bw.write("database_path=./data\n" + "log_path=./log\n" + "root_path=configuration\n" + "jdbc_server_port=99999\n" + "ignore_case_for_identifiers=disabled\n" + "regex_library=gnu.regexp\n" + "data_cache_size=2097152\n" + "max_cache_entry_size=8192\n" + "maximum_worker_threads=2\n" + "debug_log_file=debug.log\n" + "debug_level=20\n");
            bw.flush();
            bw.close();
            Connection connection = DriverManager.getConnection(url + "?create=true", username, password);
            connection.close();
        } catch (SQLException ex) {
            ExceptionHandler.handle(ex);
        } catch (IOException ex) {
            ExceptionHandler.handle(ex);
            ex.printStackTrace();
        }
    }
