    public void sendFileServer(InputStream is, OutputStream os, ComBean comBean) throws Exception {
        String fullPath = getFullPath((String) comBean.getParameters().get("fullPath"));
        File f = new File(fullPath);
        mkDirs(f);
        FileOutputStream fos = new FileOutputStream(fullPath);
        int amnt = 1460;
        byte[] buffer = new byte[amnt];
        while (true) {
            int read = is.read(buffer, 0, amnt);
            if (read >= 0) {
                fos.write(buffer, 0, read);
            } else {
                break;
            }
        }
        fos.close();
    }
