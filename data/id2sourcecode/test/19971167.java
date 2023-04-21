    public static void main(String[] args) throws IOException {
        System.out.println("The Jee greets you");
        FileInputStream theStream = new FileInputStream("test_data/TestMP3_18_ItunesAllTags_2realAPIC.mp3");
        MP3File testMP3 = new MP3File("test_data/TestMP3_15_MP3InfoExt_3APIC.mp3");
        System.out.println(testMP3.getID3v2());
        System.out.println("END");
        System.exit(0);
        Id3v2Tag test;
        if (testMP3.hasID3v2()) test = testMP3.getID3v2(); else try {
            test = new Id3v2Tag(theStream.getChannel());
            System.out.println("The header:" + test.header);
            System.out.println("Frame available:" + test.getFrameAvailability("APIC"));
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }
