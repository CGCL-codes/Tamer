    public static void moveTo(String newdir) throws IOException {
        newdir = newdir + File.separator + "chenance.db";
        BaseService.shutdown();
        File f = null;
        try {
            f = new File(BaseService.filepath);
            FileUtils.copyFile(f, new File(newdir));
        } catch (IOException e) {
            throw new IOException("Move .db file failed", e);
        }
        try {
            jdbcUrl = "jdbc:sqlite:" + newdir;
            BaseService.init();
        } catch (SQLException e) {
            throw new IOException("Reopen .db file failed", e);
        }
        try {
            FileUtils.moveFile(f, new File(BaseService.filepath + "." + new Date().getTime() + ".bak"));
        } catch (IOException e) {
            throw new IOException("Backup .db file failed", e);
        }
        BaseService.filepath = newdir;
        props.setProperty(PROP_DB_FILEPATH, newdir);
        try {
            props.store(new FileOutputStream(CHENANCE_PROPERTIES), null);
        } catch (FileNotFoundException e) {
            throw new IOException("Cannot save chenance.properties", e);
        } catch (IOException e) {
            throw new IOException("Cannot save chenance.properties", e);
        }
    }
