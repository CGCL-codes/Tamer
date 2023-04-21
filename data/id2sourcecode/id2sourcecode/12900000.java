    @Before
    public void setUp() throws Exception {
        PropertyConfigurator.configure(JIConfig.configFileName);
        ilc = new ImageListCell(photo);
        JIConfigurator.setWorkingDir(System.getProperty("user.dir") + "/.jimagick/");
        MessageDigest digest = MessageDigest.getInstance("MD5");
        md5String = null;
        FileInputStream is = new FileInputStream(photo);
        byte[] buffer = new byte[8192];
        int read = 0;
        while ((read = is.read(buffer)) > 0) {
            digest.update(buffer, 0, read);
        }
        byte[] md5sum = digest.digest();
        BigInteger bigInt = new BigInteger(1, md5sum);
        md5String = bigInt.toString(16);
        String thumbName = JIConfigurator.instance().getProperties().getProperty(JIConfigurator.JIMAGICK_WORKING_DIR, System.getProperty("user.home") + "/.jimagick/") + "thumbs/" + md5String + ".jpg";
        thumbFile = new File(thumbName);
    }
