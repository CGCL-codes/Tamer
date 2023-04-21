    public static void main(String[] args) throws IOException {
        Movie countVideo = MovieCreator.build(Channels.newChannel(SubTitleExample.class.getResourceAsStream("/count-video.mp4")));
        TextTrackImpl subTitleEng = new TextTrackImpl();
        subTitleEng.getTrackMetaData().setLanguage("eng");
        subTitleEng.getSubs().add(new TextTrackImpl.Line(5000, 6000, "Five"));
        subTitleEng.getSubs().add(new TextTrackImpl.Line(8000, 9000, "Four"));
        subTitleEng.getSubs().add(new TextTrackImpl.Line(12000, 13000, "Three"));
        subTitleEng.getSubs().add(new TextTrackImpl.Line(16000, 17000, "Two"));
        subTitleEng.getSubs().add(new TextTrackImpl.Line(20000, 21000, "one"));
        countVideo.addTrack(subTitleEng);
        TextTrackImpl subTitleDeu = SrtParser.parse(SubTitleExample.class.getResourceAsStream("/count-subs-deutsch.srt"));
        subTitleDeu.getTrackMetaData().setLanguage("deu");
        countVideo.addTrack(subTitleDeu);
        IsoFile out = new DefaultMp4Builder().build(countVideo);
        FileOutputStream fos = new FileOutputStream(new File("output.mp4"));
        out.getBox(fos.getChannel());
        fos.close();
    }
