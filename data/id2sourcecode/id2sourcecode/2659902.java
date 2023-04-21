    public void write(File dest) {
        try {
            TextureIO.write(readTextureData, dest);
            rewindPixelBuffer();
        } catch (IOException ex) {
            throw new RuntimeException("can not write to file: " + dest.getAbsolutePath(), ex);
        }
    }
