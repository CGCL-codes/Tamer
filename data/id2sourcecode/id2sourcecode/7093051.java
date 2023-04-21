    public boolean retrFile(String fullPath, String toFile) throws Exception {
        try {
            ComBean comBean = new ComBean();
            comBean.setCommand("retrFile");
            Hashtable params = new Hashtable();
            params.put("fullPath", fullPath);
            comBean.setParameters(params);
            ComBean answer = sendCommand(comBean);
            if (answer.getErrorCode() == 0) {
                FileOutputStream fos = new FileOutputStream(toFile);
                int amnt = 1460;
                byte[] buffer = new byte[amnt];
                while (true) {
                    int read = answer.getInputStream().read(buffer, 0, amnt);
                    if (read >= 0) {
                        fos.write(buffer, 0, read);
                    } else {
                        break;
                    }
                }
                fos.close();
                return true;
            } else {
                return false;
            }
        } finally {
            cleanUpConnection();
        }
    }
