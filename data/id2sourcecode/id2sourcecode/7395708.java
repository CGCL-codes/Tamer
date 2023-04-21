    public static void initRolesMappingFile() throws IOException {
        String webConfigPath = ServerConfigLocator.locate().getServerHomeDir().getAbsolutePath() + "/conf/";
        System.setProperty("dcm4chee-web3.cfg.path", webConfigPath);
        File f = new File(ROLE_MAPPING_FILENAME);
        if (!f.isAbsolute()) f = new File(webConfigPath, f.getPath());
        if (f.exists()) return;
        f.getParentFile().mkdirs();
        FileChannel fos = null;
        InputStream is = null;
        try {
            URL url = WASPTestUtil.class.getResource("/roles-test.json");
            is = url.openStream();
            ReadableByteChannel inCh = Channels.newChannel(is);
            fos = new FileOutputStream(f).getChannel();
            int pos = 0;
            while (is.available() > 0) pos += fos.transferFrom(inCh, pos, is.available());
        } finally {
            try {
                if (is != null) is.close();
            } catch (Exception ignore) {
            }
            try {
                if (fos != null) fos.close();
            } catch (Exception ignore) {
            }
        }
    }
