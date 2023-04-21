    public static boolean fileCopy(InputStream is, String sFileDst) {
        boolean ok = true;
        FileOutputStream fos = null;
        try {
            int len = 32768;
            byte[] buff = new byte[len];
            boolean append = false;
            fos = new FileOutputStream(sFileDst, append);
            while (0 < (len = is.read(buff))) fos.write(buff, 0, len);
            fos.flush();
        } catch (IOException e) {
            JdxLog.logError(e);
            ok = false;
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JdxLog.logError(ex);
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JdxLog.logError(ex);
                }
            }
        }
        return ok;
    }
