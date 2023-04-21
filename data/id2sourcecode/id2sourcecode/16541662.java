    private String getContentType(AudioFormat format) {
        String encoding = null;
        if (format.getEncoding() == AudioFormat.Encoding.ULAW) {
            encoding = "MULAW";
        } else if (format.getEncoding() == AudioFormat.Encoding.PCM_SIGNED) {
            encoding = "L16";
        }
        return "AUDIO/" + encoding + "; CHANNELS=" + format.getChannels() + "; RATE=" + (int) format.getSampleRate() + "; BIG=" + format.isBigEndian();
    }
