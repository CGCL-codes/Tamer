    private void doStore(String path, File file) throws Exception {
        if (file.isDirectory()) {
            File[] f = file.listFiles();
            for (int i = 0; i < f.length; i++) {
                doStore(path + '/' + f[i].getName(), f[i]);
            }
            return;
        }
        MessageDigest md = MessageDigest.getInstance("MD5");
        Dataset ds = loadDataset(file, md);
        storage.store(CALLING_AET, CALLED_AET, ds, RETRIEVE_AET, "/", path.substring(1), (int) file.length(), md.digest());
    }
