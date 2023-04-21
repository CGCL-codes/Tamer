    void revert(String dateString) throws SQLException, ServletException {
        try {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = format.parse(dateString);
            File dataDir = new File(jdbc.SQLqueryString("SELECT @@datadir"));
            File dbDir = new File(dataDir, dbName);
            File MySQLdir = dataDir.getParentFile();
            File bkpDir = new File(new File(MySQLdir, "dataBKP"), dbName);
            File restoreFile = new File(bkpDir, "restoreDates.txt");
            String restoreFileString = Util.readFile(restoreFile);
            String[] restoreDates = restoreFileString.split("\n");
            restoreFileString += "\n" + format.format(new Date());
            Util.writeFile(restoreFile, restoreFileString);
            Date from = null;
            for (int i = 0; i < restoreDates.length; i++) {
                Date restored = format.parse(restoreDates[i]);
                if (!restored.after(date)) from = restored; else if (from == null) myAssert(false, "Attempt to revert to " + date + ", which predates the full backup of " + restored); else break;
            }
            StringBuffer logFiles = new StringBuffer();
            File[] logFileNames = dataDir.listFiles(Util.getFilenameFilter(".*bin\\.\\d+"));
            for (int i = 0; i < logFileNames.length; i++) {
                File file = logFileNames[i];
                if (new Date(file.lastModified()).after(from)) logFiles.append(" ").append(file.getName());
            }
            exec("net stop \"MySQL\"");
            String[] corruptTables = { "facet", "raw_facet", "raw_facet_type", "raw_item_facet", "globals", "item", "item_facet", "item_facet_heap", "item_facet_type_heap" };
            for (int i = 0; i < corruptTables.length; i++) {
                exec("del /S /Q \"" + corruptTables[i] + ".*", dbDir);
                exec("copy \"" + bkpDir + "\\" + corruptTables[i] + ".* .", dbDir);
            }
            exec("net start \"MySQL\"");
            Pattern p = Pattern.compile(".*user=(.+?)&.*password=(.+?)&");
            Matcher m = p.matcher(jdbc.toString());
            m.find();
            String user = m.group(1);
            String pass = m.group(2);
            exec("..\\bin\\mysqlbinlog --database=" + dbName + " --start-date=\"" + format.format(from) + "\" --stop-date=\"" + format.format(date) + "\"" + logFiles + " | ..\\bin\\mysql -u " + user + " -p" + pass, dataDir);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
