    public static void main(String[] args) throws IOException {
        Movie video = MovieCreator.build(Channels.newChannel(Mp4WithAudioDelayExample.class.getResourceAsStream("/count-video.mp4")));
        Movie audio = MovieCreator.build(Channels.newChannel(Mp4WithAudioDelayExample.class.getResourceAsStream("/count-english-audio.mp4")));
        List<Track> videoTracks = video.getTracks();
        video.setTracks(new LinkedList<Track>());
        List<Track> audioTracks = audio.getTracks();
        for (Track videoTrack : videoTracks) {
            video.addTrack(new AppendTrack(videoTrack, videoTrack));
        }
        for (Track audioTrack : audioTracks) {
            audioTrack.getTrackMetaData().setStartTime(10.0);
            video.addTrack(audioTrack);
        }
        IsoFile out = new DefaultMp4Builder().build(video);
        FileOutputStream fos = new FileOutputStream(new File(String.format("output.mp4")));
        out.getBox(fos.getChannel());
        fos.close();
    }
