    public void retrFileServer(InputStream is, OutputStream os, ComBean comBean) throws Exception {
        String fullPath = getFullPath((String) comBean.getParameters().get("fullPath"));
        FileInputStream fis = new FileInputStream(fullPath);
        ObjectOutputStream oos = new ObjectOutputStream(os);
        ComBean answer = new ComBean();
        answer.setErrorCode(0);
        answer.setVersionNumber(ComBean.version);
        oos.writeObject(answer);
        int amnt = 1460;
        byte[] buffer = new byte[amnt];
        while (true) {
            int read = fis.read(buffer, 0, amnt);
            if (read >= 0) {
                os.write(buffer, 0, read);
            } else {
                break;
            }
        }
        fis.close();
    }
