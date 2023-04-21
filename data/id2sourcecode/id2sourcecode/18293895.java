    @Override
    public void startBatch(Batch batch) {
        try {
            close();
            extractingFile = new File(dir, toFileName(batch, true));
            if (extractingFile.exists()) {
                extractingFile.delete();
            }
            channel = new FileOutputStream(extractingFile).getChannel();
            super.startBatch(batch);
        } catch (IOException ex) {
            throw new IoException(ex);
        }
    }
