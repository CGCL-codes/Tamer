    public void testCreateNewId3v2Tag() {
        File testfile = new File("data/JUnit_Id3v2TagTest.id3");
        try {
            testfile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            fail("Couldn't create File");
        }
        try {
            new Id3v2Tag(new FileInputStream(testfile).getChannel());
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
            fail("FileNotFound");
        } catch (InstantiationException e) {
            e.printStackTrace();
            fail("No ID3 Tag found");
        }
    }
