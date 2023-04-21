    public static void main(String[] args) throws IOException {
        AACTrackImpl aacTrack = new AACTrackImpl(new FileInputStream("/Users/magnus/Projects/castlabs/cff/Solekai015_1920_29_75x75_v2/Solekai_BeautifulTension_15sec_160k.aac"));
        Movie m = new Movie();
        m.addTrack(aacTrack);
        DefaultMp4Builder mp4Builder = new DefaultMp4Builder();
        IsoFile isoFile = mp4Builder.build(m);
        FileOutputStream fos = new FileOutputStream("output.mp4");
        isoFile.getBox(fos.getChannel());
        fos.close();
    }
