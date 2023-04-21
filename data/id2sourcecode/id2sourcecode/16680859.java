    public String getCachedCertificate(String certName) {
        ByteArrayOutputStream cert = new ByteArrayOutputStream();
        try {
            FileInputStream fin = new FileInputStream(PATH_PREFIX + certName + EXT);
            byte[] buff = new byte[2048];
            int readLen = 0;
            while ((readLen = fin.read(buff)) != -1) {
                cert.write(buff, 0, readLen);
            }
            fin.close();
        } catch (FileNotFoundException e) {
            Log.e(TAG, Log.getStackTraceString(e));
        } catch (IOException e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
        return cert.toString();
    }
