    public static void main(String[] args) throws IOException {
        Movie video = MovieCreator.build(Channels.newChannel(Mp4WithAudioDelayExample.class.getResourceAsStream("/example-sans-amf0.mp4")));
        Properties props = new Properties();
        props.load(MuxVideoWithAmf0.class.getResourceAsStream("/amf0track.properties"));
        HashMap<Long, byte[]> samples = new HashMap<Long, byte[]>();
        for (String key : props.stringPropertyNames()) {
            samples.put(Long.parseLong(key), Base64.decodeBase64(props.getProperty(key)));
        }
        Track amf0Track = new Amf0Track(samples);
        amf0Track.getTrackMetaData().setStartTime(2400);
        video.addTrack(amf0Track);
        FragmentedMp4Builder fragmentedMp4Builder = new FragmentedMp4Builder();
        fragmentedMp4Builder.setIntersectionFinder(new TwoSecondIntersectionFinder());
        IsoFile out = fragmentedMp4Builder.build(video);
        FileOutputStream fos = new FileOutputStream(new File(String.format("output.mp4")));
        out.getBox(fos.getChannel());
        fos.close();
    }
