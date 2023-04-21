    XFontPeer2(String name, int style, int size) {
        super(name, style, size);
        try {
            File fontfile = new File("/usr/share/fonts/truetype/ttf-bitstream-vera/Vera.ttf");
            FileInputStream in = new FileInputStream(fontfile);
            FileChannel ch = in.getChannel();
            ByteBuffer buffer = ch.map(FileChannel.MapMode.READ_ONLY, 0, fontfile.length());
            fontDelegate = FontFactory.createFonts(buffer)[0];
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
