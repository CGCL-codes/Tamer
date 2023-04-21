    public void testId3v2Tag() {
        System.out.println("The Jee greets you");
        File directory = new File("test_data");
        System.out.println("directory:" + directory.getAbsolutePath());
        File[] files = directory.listFiles(new AudioFileFilter(false));
        System.out.println("Found:" + files.length);
        for (int i = 0; i < files.length; i++) {
            FileInputStream theStream = null;
            String[] excludes = { "07 Consuming Fire.mp3", "01 Here We Go.mp3", "01 I Turn to You (Louie Devito Mix).mp3", "Irene.mp3", "03 Sadie Hawkins Dance.mp3", "01 The Final Slowdance.mp3", "08 The Reason.mp3", "TestMP3_17_Musicmatch9AllTags_includingrealAPIC.mp3" };
            try {
                System.out.print("Name:" + files[i].getAbsolutePath());
                int j = 0;
                boolean con = false;
                while (j < excludes.length) {
                    if (excludes[j++].equals(files[i].getName())) {
                        con = true;
                        System.out.println("...skipped");
                        break;
                    }
                }
                if (con) continue;
                theStream = new FileInputStream(files[i]);
                System.out.println(" done");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            if (theStream == null) System.out.println("ERRRRRORRRR");
            Id3v2Tag test;
            try {
                test = new Id3v2Tag(theStream.getChannel());
                assertEquals(false, Id3v2Tag.faulty);
                System.out.println(test.header);
                String getFrame = "TDRC";
                if (test != null) {
                    System.out.println("Frame available:" + test.getFrameAvailability(getFrame));
                    if (test.getFrameAvailability(getFrame)) System.out.println(test.getFrame(getFrame));
                }
            } catch (InstantiationException e1) {
                e1.printStackTrace();
            }
        }
    }
