    void copyTextureData2File() throws IOException {
        if (!readBufferUtil.isValid()) {
            return;
        }
        File file = File.createTempFile("shot" + shotNum + "-", ".ppm");
        readBufferUtil.write(file);
        System.out.println("Wrote: " + file.getAbsolutePath() + ", ...");
        shotNum++;
    }
