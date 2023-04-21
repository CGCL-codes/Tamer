    public static void main(String[] args) throws IOException {
        EC3TrackImpl track = new EC3TrackImpl(new FileInputStream("/home/sannies/Solekai020_1920_23_75x1_Dolby_v2/Dolby_Channel-Check_Voice_8-ch_Discrete_24bit_48kHz_dn24_448.ec3"));
        Movie m = new Movie();
        m.addTrack(track);
        DefaultMp4Builder mp4Builder = new DefaultMp4Builder();
        IsoFile isoFile = mp4Builder.build(m);
        FileOutputStream fos = new FileOutputStream("output.mp4");
        isoFile.getBox(fos.getChannel());
        fos.close();
    }
