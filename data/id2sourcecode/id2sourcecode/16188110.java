    public void testGetRawData() {
        FileInputStream theStream = null;
        try {
            theStream = new FileInputStream("test_data/tagTestCases/basefiles/test_TIT2.mp3");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (theStream == null) System.out.println("ERRRRRORRRR");
        Id3v2Tag test;
        try {
            test = new Id3v2Tag(theStream.getChannel());
            assertEquals(false, Id3v2Tag.faulty);
            String getFrame = "TIT2";
            String should = "";
            if (test != null && test.getFrameAvailability(getFrame)) should = test.getFrame(getFrame).toString();
            Id3FrameTIT2 tit2frame = new Id3FrameTIT2(null, new Id3v2FrameHeader("TIT2"));
            tit2frame.setTextEncoding(new FieldEncoding(FieldEncoding.ISO88591));
            tit2frame.setTitle("junit test");
            assertEquals(should, tit2frame.toString());
            FileInputStream shoudStream = null;
            try {
                shoudStream = new FileInputStream("test_data/tagTestCases/testTIT2.id3frame");
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
            byte[] shouldByte = null;
            try {
                shouldByte = new byte[shoudStream.available()];
                shoudStream.read(shouldByte);
            } catch (IOException e3) {
                e3.printStackTrace();
            }
            byte[] isByte = tit2frame.getAllData().toByteArray();
            assertEquals(shouldByte.length, isByte.length);
            for (int i = 0; i < shouldByte.length; i++) {
                System.out.println(shouldByte[i] + "=" + isByte[i]);
                assertEquals(shouldByte[i], isByte[i]);
            }
        } catch (InstantiationException e2) {
            e2.printStackTrace();
            fail("Couldn't create Tag");
        }
    }
