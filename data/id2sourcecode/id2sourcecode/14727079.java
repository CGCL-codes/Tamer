    @BeforeClass
    public static void initialize() throws Exception {
        new java.io.File(TEMP_SAVE_LOCATION).delete();
        original_file = new FileInputStream(ORIGINAL_FILE).getChannel();
    }
