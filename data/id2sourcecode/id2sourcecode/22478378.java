    public static void main(String[] args) throws IOException {
        H264TrackImpl h264Track = new H264TrackImpl(new FileInputStream("/Users/magnus/Projects/castlabs/cff/DTS_Paint_HD1/DTS_Paint_HD1.h264"));
        AACTrackImpl aacTrack = new AACTrackImpl(new FileInputStream("/Users/magnus/Projects/castlabs/cff/DTS_Paint_HD1/DTS_Paint.aac"));
        Movie m = new Movie();
        m.addTrack(h264Track);
        m.addTrack(aacTrack);
        {
            IsoFile out = new DefaultMp4Builder().build(m);
            FileOutputStream fos = new FileOutputStream(new File("h264_output.mp4"));
            out.getBox(fos.getChannel());
            fos.close();
        }
        {
            FragmentedMp4Builder fragmentedMp4Builder = new FragmentedMp4Builder();
            fragmentedMp4Builder.setIntersectionFinder(new SyncSampleIntersectFinderImpl());
            IsoFile out = fragmentedMp4Builder.build(m);
            FileOutputStream fos = new FileOutputStream(new File("h264_frag_output.mp4"));
            out.getBox(fos.getChannel());
            fos.close();
        }
    }
