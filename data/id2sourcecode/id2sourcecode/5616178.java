    public void surface2File(String basename) throws IOException {
        if (!readBufferUtil.isValid()) {
            return;
        }
        File file = File.createTempFile(basename + shotNum + "-", ".ppm");
        readBufferUtil.write(file);
        System.err.println("Wrote: " + file.getAbsolutePath() + ", ...");
        shotNum++;
    }
