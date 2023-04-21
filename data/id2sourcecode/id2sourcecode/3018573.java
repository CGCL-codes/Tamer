    private void executeMidStreamCreationTest() {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(WaveDataTest.class.getClassLoader().getResource(filePath));
            int totalSize = ais.getFormat().getChannels() * (int) ais.getFrameLength() * ais.getFormat().getSampleSizeInBits() / 8;
            int skip = totalSize / 4;
            long skipped = ais.skip(skip);
            WaveData wd = WaveData.create(ais);
            if (wd == null) {
                System.out.println("executeMidStreamCreationTest::success");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
