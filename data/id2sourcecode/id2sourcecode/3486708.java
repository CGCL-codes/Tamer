    public void testConvertSilentOggWithAudioOutStream() throws Exception {
        final AudioInputStream inAIStreamOgg = AudioSystem.getAudioInputStream(_sourceFileOgg);
        final AudioFormat destAudioFormatPCM = new AudioFormat(44100.0F, 16, 1, true, false);
        final AudioInputStream inAIStreamPCM = AudioSystem.getAudioInputStream(destAudioFormatPCM, inAIStreamOgg);
        final AudioOutputStream outAOStreamWavPCM = AudioSystemShadow.getAudioOutputStream(AudioFileFormat.Type.WAVE, destAudioFormatPCM, AudioSystem.NOT_SPECIFIED, _destFileWav);
        class StreamPump extends Thread {

            boolean isRunFinished;

            StreamPump() {
                super("SilenceTest-StreamPump");
            }

            public void run() {
                int readCnt;
                final byte[] buf = new byte[65536];
                int cnt = 0;
                try {
                    while ((readCnt = inAIStreamPCM.read(buf, 0, buf.length)) != -1) {
                        outAOStreamWavPCM.write(buf, 0, readCnt);
                        cnt++;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    fail("Exception pumping streams: " + e.getMessage());
                }
                isRunFinished = true;
            }
        }
        ;
        StreamPump t = new StreamPump();
        t.start();
        final int maxWait = 60;
        int waitCnt = 0;
        while (t.isAlive() && waitCnt++ < maxWait) {
            Thread.sleep(1000);
        }
        outAOStreamWavPCM.close();
        inAIStreamPCM.close();
        inAIStreamOgg.close();
        assertTrue("Converted wav file is empty: " + _destFileWav.getAbsolutePath(), _destFileWav.length() > 0);
        assertTrue("Conversion never finished run() method", t.isRunFinished);
        assertTrue("Conversion timeout expired", waitCnt < maxWait);
        final AudioInputStream aisDest = AudioSystem.getAudioInputStream(_destFileWav);
        playStream(aisDest);
    }
