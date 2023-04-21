    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        File f = new File("./testdata/TOC1.dat");
        FileInputStream fs = new FileInputStream(f);
        fs.getChannel().read(toc);
        toc.rewind();
        fs.close();
        int i = 0;
        while (toc.remaining() > S2TableOfContentsLine.LINE_LENGTH) {
            tocLines.add(new S2TableOfContentsLine(toc, i++));
        }
    }
