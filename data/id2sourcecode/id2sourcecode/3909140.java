    private static void testMusicXmlParser() {
        File fileXML = new File("/users/epsobolik/documents/binchois.xml");
        try {
            FileInputStream fisXML = new FileInputStream(fileXML);
            FileChannel fc = fisXML.getChannel();
            ByteBuffer buf = ByteBuffer.allocate((int) fc.size());
            fc.read(buf);
            buf.flip();
            MusicXmlParser MusicXMLIn = new MusicXmlParser();
            MusicStringRenderer MusicStringOut = new MusicStringRenderer();
            MusicXMLIn.addParserListener(MusicStringOut);
            MusicXMLIn.parse(fileXML);
            PatternInterface p = MusicStringOut.getPattern();
            p.insert("T60");
            System.out.println(p.toString());
            System.out.print('\n');
            Player player = new Player();
            player.play(p);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
