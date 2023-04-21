    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        File f = new File("./testdata/bootrecord1.dat");
        FileInputStream fs = new FileInputStream(f);
        partTable = fs.getChannel();
        f = new File("./testdata/first_sect_t1.dat");
        fs = new FileInputStream(f);
        nonPartTable = fs.getChannel();
        f = new File("./testdata/image.img");
        fs = new FileInputStream(f);
        complexImage = fs.getChannel();
    }
