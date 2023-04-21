    public static Audio getStreamingAudio(String format, URL url) throws IOException {
        init();
        if (format.equals(OGG)) {
            return SoundStore.get().getOggStream(url);
        }
        if (format.equals(MOD)) {
            return SoundStore.get().getMOD(url.openStream());
        }
        if (format.equals(XM)) {
            return SoundStore.get().getMOD(url.openStream());
        }
        throw new IOException("Unsupported format for streaming Audio: " + format);
    }
