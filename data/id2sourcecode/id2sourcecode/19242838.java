    public void testFormatConversion() throws Exception {
        boolean ok;
        AudioFormat af_source = new AudioFormat(AudioFormat.Encoding.PCM_UNSIGNED, 1f, 2, 3, 4, 5f, true);
        AudioFormat.Encoding[] aafe = AudioSystem.getTargetEncodings(AudioFormat.Encoding.PCM_UNSIGNED);
        ok = false;
        for (int i = 0; i < aafe.length; i++) {
            if (aafe[i].equals(AudioFormat.Encoding.PCM_SIGNED)) {
                ok = true;
                break;
            }
        }
        assertTrue(ok);
        assertTrue(AudioSystem.isConversionSupported(AudioFormat.Encoding.PCM_SIGNED, af_source));
        AudioFormat[] aaf = AudioSystem.getTargetFormats(AudioFormat.Encoding.PCM_UNSIGNED, af_source);
        ok = false;
        for (int i = 0; i < aaf.length; i++) {
            if (aaf[i].getSampleRate() == 10f && aaf[i].getSampleSizeInBits() == 2 && aaf[i].getChannels() == 30 && aaf[i].getFrameSize() == 40 && aaf[i].getFrameRate() == 50f) {
                ok = true;
                break;
            }
        }
        assertTrue(ok);
    }
