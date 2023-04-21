    public static void main(String[] args) throws IOException {
        Movie countVideo = MovieCreator.build(Channels.newChannel(MuxExample.class.getResourceAsStream("/count-video.mp4")));
        Movie countAudioDeutsch = MovieCreator.build(Channels.newChannel(MuxExample.class.getResourceAsStream("/count-deutsch-audio.mp4")));
        Movie countAudioEnglish = MovieCreator.build(Channels.newChannel(MuxExample.class.getResourceAsStream("/count-english-audio.mp4")));
        Track audioTrackDeutsch = countAudioDeutsch.getTracks().get(0);
        audioTrackDeutsch.getTrackMetaData().setLanguage("deu");
        Track audioTrackEnglish = countAudioEnglish.getTracks().get(0);
        audioTrackEnglish.getTrackMetaData().setLanguage("eng");
        countVideo.addTrack(audioTrackDeutsch);
        countVideo.addTrack(audioTrackEnglish);
        {
            IsoFile out = new DefaultMp4Builder().build(countVideo);
            FileOutputStream fos = new FileOutputStream(new File("output.mp4"));
            out.getBox(fos.getChannel());
            fos.close();
        }
        {
            FragmentedMp4Builder fragmentedMp4Builder = new FragmentedMp4Builder();
            fragmentedMp4Builder.setIntersectionFinder(new SyncSampleIntersectFinderImpl());
            IsoFile out = fragmentedMp4Builder.build(countVideo);
            FileOutputStream fos = new FileOutputStream(new File("output-frag.mp4"));
            out.getBox(fos.getChannel());
            fos.close();
        }
    }
