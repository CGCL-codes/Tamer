    public void testConvertTruncateOggWithAudioOutStream() throws Exception {
        final AudioInputStream inAIStreamOgg = AudioSystem.getAudioInputStream(_sourceFileOgg);
        final AudioFormat destAudioFormatPCM = new AudioFormat(22050.0F, 16, 1, true, false);
        final AudioInputStream inAIStreamPCM = AudioSystem.getAudioInputStream(destAudioFormatPCM, inAIStreamOgg);
        final AudioOutputStream outAOStreamWavPCM = AudioSystemShadow.getAudioOutputStream(AudioFileFormat.Type.WAVE, destAudioFormatPCM, AudioSystem.NOT_SPECIFIED, _destFileWav);
        long readCntPCMTotal = 0;
        int readCnt;
        final byte[] buf = new byte[4 * 1024];
        try {
            while ((readCnt = inAIStreamPCM.read(buf, 0, buf.length)) != -1) {
                readCntPCMTotal += readCnt;
                outAOStreamWavPCM.write(buf, 0, readCnt);
            }
        } catch (IOException e) {
            e.printStackTrace();
            fail("Exception pumping streams: " + e.getMessage());
        }
        outAOStreamWavPCM.close();
        inAIStreamPCM.close();
        inAIStreamOgg.close();
        assertTrue("Converted wav file is empty: " + _destFileWav.getAbsolutePath(), _destFileWav.length() > 0);
        playStream(_destFileWav);
        assertEquals("Missing some PCM data from decoded Vorbis stream.", 369664, readCntPCMTotal);
    }
