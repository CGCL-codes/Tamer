    public boolean sendFile(String fromFile, String fullPath) throws Exception {
        try {
            ComBean comBean = new ComBean();
            comBean.setCommand("sendFile");
            Hashtable params = new Hashtable();
            params.put("fullPath", fullPath);
            comBean.setParameters(params);
            File f = new File(fromFile);
            if (f.exists()) {
                OutputStream os = sendCommandOS(comBean);
                if (os != null) {
                    FileInputStream fis = new FileInputStream(fromFile);
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
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } finally {
            cleanUpConnection();
        }
    }
