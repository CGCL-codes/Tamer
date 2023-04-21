    private void reMap() throws IOException {
        if (file.length() > Integer.MAX_VALUE) {
            throw new RuntimeException("File over 2GB is not supported yet");
        }
        int oldPos = 0;
        if (mapped != null) {
            oldPos = mapped.position();
            mapped.force();
            unMap();
        }
        mapped = file.getChannel().map(mode, 0, file.length());
        if (SysProperties.NIO_LOAD_MAPPED) {
            mapped.load();
        }
        mapped.position(oldPos);
    }
