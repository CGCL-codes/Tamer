    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        FileInputStream fs = new FileInputStream(new File("./testdata/names_t1.dat"));
        fs.getChannel().read(namesTSDV1);
        namesTSDV1.rewind();
        fs.close();
        fs = new FileInputStream(new File("./testdata/names_s2.dat"));
        fs.getChannel().read(namesTSDV2);
        namesTSDV2.rewind();
        namesTSDV2.order(ByteOrder.LITTLE_ENDIAN);
        fs.close();
        fs = new FileInputStream(new File("./testdata/names_hd8+.dat"));
        fs.getChannel().read(namesTSDV2p);
        namesTSDV2p.rewind();
        namesTSDV2p.order(ByteOrder.LITTLE_ENDIAN);
        fs.close();
        fs = new FileInputStream(new File("./testdata/names_upd1578.dat"));
        fs.getChannel().read(namesUpd1578);
        namesUpd1578.rewind();
        namesUpd1578.order(ByteOrder.LITTLE_ENDIAN);
        fs.close();
    }
