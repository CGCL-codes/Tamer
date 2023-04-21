    public final void Open(URL url) throws Exception {
        input = new DataInputStream(new BufferedInputStream(url.openStream()));
        MdfHeader mdfheader = new MdfHeader();
        InitRead();
        mdfheader.read(input);
        format = mdfheader.format;
        ntrks = mdfheader.ntrks;
        time = mdfheader.time;
    }
