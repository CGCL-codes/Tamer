    private FileContent readImpl(File toRead, Period timeout) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(toRead);
        FileChannel fileChannel = fileInputStream.getChannel();
        TimingOutTask task = null;
        FileContent fileContents = null;
        TimeLimitedInputStream timeLimitedInputStream = null;
        InputStream channelBackedInputStream = Channels.newInputStream(fileChannel);
        timeLimitedInputStream = new TimeLimitedInputStream(channelBackedInputStream);
        task = createAndScheduleTimingOutTask(timeout);
        timeLimitedInputStream.setTimerTask(task);
        fileContents = new FileContent(timeLimitedInputStream, toRead);
        return fileContents;
    }
