    public AbstractSerializer(final R reader, final W writer) {
        this.reader = reader;
        this.writer = writer;
        fileReader = new FileChooser(reader.getFormats());
        fileReader.setMultiSelectionEnabled(false);
        fileWriter = new FileChooser(writer.getFormats());
        fileWriter.setMultiSelectionEnabled(false);
    }
