    @Override
    public boolean tryLoadFile(String fileName) {
        try {
            File fileIn = new File(fileName);
            try {
                IStreamHandler ash = null;
                if (fileName.toLowerCase().endsWith(".wav")) ash = new WavStreamHandler(fileIn); else if (fileName.toLowerCase().endsWith(".mp3")) ash = new Mp3StreamHandler(fileIn); else if (fileName.toLowerCase().endsWith(".flac")) ash = new FlacStreamHandler(fileIn);
                if (ash == null) return false;
                length = (int) ash.getFrameLength();
                numChannels = ash.getChannels();
                sampleRate = ash.getSampleRate();
                sampleData = new float[numChannels][length];
                ash.loadSample(sampleData);
                return true;
            } catch (Throwable ex) {
                logger.log(Level.SEVERE, "Exception reading sample", ex);
                return false;
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Exception reading sample", ex);
            return false;
        }
    }
