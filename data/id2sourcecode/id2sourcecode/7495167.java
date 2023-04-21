    @Override
    public void begin(OutgoingBatch batch, BufferedWriter writer) throws IOException {
        Util.write(writer, CsvConstants.CHANNEL, Util.DELIMITER, batch.getChannelId());
        writer.newLine();
        Util.write(writer, CsvConstants.BATCH, Util.DELIMITER, Long.toString(batch.getBatchId()));
        writer.newLine();
    }
