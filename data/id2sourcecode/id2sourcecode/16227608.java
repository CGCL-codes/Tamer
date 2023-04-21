    @Override
    public void save(String filename, EmbeddingProperties props) {
        if (!filename.endsWith(".png")) filename += ".png";
        writer = new PngWriter(filename, reader.imgInfo);
        writer.props = props;
        writer.setOverrideFile(true);
        writer.prepare(reader);
        for (int i = 0; i < imagelines.length; i++) {
            writer.writeRow(imagelines[i]);
        }
        writer.end();
    }
