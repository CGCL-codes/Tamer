    private void createTempFiles() throws IOException {
        final AudioFileDescr afd = new AudioFileDescr();
        afd.type = AudioFileDescr.TYPE_WAVE64;
        afd.rate = getRate();
        afd.bitsPerSample = 32;
        afd.sampleFormat = AudioFileDescr.FORMAT_FLOAT;
        if (singleFile) {
            afd.channels = getChannelNum();
            afd.file = IOUtil.createTempFile();
            tempF = new AudioFile[] { AudioFile.openAsWrite(afd) };
        } else {
            AudioFileDescr afd2;
            final AudioFile[] tempF2 = new AudioFile[channelMaps.length];
            for (int i = 0; i < channelMaps.length; i++) {
                afd2 = new AudioFileDescr(afd);
                afd2.channels = channelMaps[i].length;
                afd2.file = IOUtil.createTempFile();
                tempF2[i] = AudioFile.openAsWrite(afd2);
            }
            tempF = tempF2;
        }
    }
