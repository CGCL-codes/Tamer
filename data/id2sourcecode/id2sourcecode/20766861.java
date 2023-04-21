    private void slowOutput(OutputStream out, File file) throws Exception {
        FileInputStream in = new FileInputStream(file);
        byte[] buffer = new byte[2048];
        int read = -1;
        long aSleep = 10 * (long) Configuration.getPrefs().getInt(ConfigConstants.MODE_PROXY + ConfigConstants.SETTING_DELAY, 0);
        while ((read = in.read(buffer)) > 0) {
            long start = System.currentTimeMillis();
            if (aSleep > 0) Thread.sleep(aSleep);
            out.write(buffer, 0, read);
        }
        in.close();
    }
