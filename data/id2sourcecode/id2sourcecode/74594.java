    private void uploadImage(URL[] files, URL endpoint, String parameter, Properties prop) throws RemoteException, IOException {
        for (int i = 0; i < files.length; i++) {
            String filename = FileUtils.getFilename(files[i]);
            String outputFile = System.currentTimeMillis() + "_" + filename;
            FileUtils.copyFile(files[i], new File(this.api.getStorage() + "/" + outputFile));
        }
    }
