    @Override
    public void save(String filename) {
        if (!filename.endsWith(".png")) filename += ".png";
        writer = new PngWriter(filename, reader.imgInfo);
        writer.setOverrideFile(true);
        writer.prepare(reader);
        for (int i = 0; i < imagelines.length; i++) {
            writer.writeRow(imagelines[i]);
        }
        writer.end();
    }
