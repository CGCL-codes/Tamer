    public Experiment(String soundFilename) throws Exception {
        File file = new File(soundFilename);
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
        int frameLength = (int) audioInputStream.getFrameLength();
        int frameSize = (int) audioInputStream.getFormat().getFrameSize();
        int numChannels = audioInputStream.getFormat().getChannels();
        int sampleSize = audioInputStream.getFormat().getSampleSizeInBits();
        System.out.println("Frame length: " + frameLength);
        System.out.println("Frame size: " + frameSize);
        System.out.println("Channels: " + numChannels);
        System.out.println("Sample size: " + sampleSize);
        System.out.println("DONE!");
    }
