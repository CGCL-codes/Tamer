    public static Sample createFromFile(String pathfile) throws FileNotFoundException, IOException {
        Sample sample = new Sample("", "", "");
        FileInputStream fis = new FileInputStream(pathfile);
        FileChannel fc = fis.getChannel();
        sample.header.readFromFileChannel(fc);
        sample.config.readFromFileChannel(fc);
        sample.data = fc.map(FileChannel.MapMode.READ_ONLY, 0x30, sample.config.getFramesNumber() * sample.config.getWordLength() * (sample.config.getStereo() == false ? 1 : 2));
        sample.setAudioFormat();
        return sample;
    }
