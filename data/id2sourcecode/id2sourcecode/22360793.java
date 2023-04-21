    public MJPEGGenerator(File aviFile, int width, int height, double framerate, int numFrames) throws IOException {
        this.aviFile = aviFile;
        this.width = width;
        this.height = height;
        this.framerate = framerate;
        this.numFrames = numFrames;
        aviOutput = new FileOutputStream(aviFile);
        aviChannel = aviOutput.getChannel();
        RIFFHeader rh = new RIFFHeader();
        aviOutput.write(rh.toBytes());
        aviOutput.write(new AVIMainHeader().toBytes());
        aviOutput.write(new AVIStreamList().toBytes());
        aviOutput.write(new AVIStreamHeader().toBytes());
        aviOutput.write(new AVIStreamFormat().toBytes());
        aviOutput.write(new AVIJunk().toBytes());
        aviMovieOffset = aviChannel.position();
        aviOutput.write(new AVIMovieList().toBytes());
        indexlist = new AVIIndexList();
    }
