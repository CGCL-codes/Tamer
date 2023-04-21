    public void exportDataThread(String rbnbServer, List<String> channels, double startTime, double endTime, File file, DataFileWriter writer, ProgressWindow progressWindow) throws SAPIException, IOException {
        RBNBReader reader = null;
        try {
            reader = new RBNBReader(rbnbServer, channels, startTime, endTime);
            List<DataChannel> channelMetadata = RBNBController.getInstance().getMetadataManager().getChannels(channels);
            writer.init(channelMetadata, startTime, endTime, file);
            progressWindow.setProgress(0);
            NumericDataSample sample;
            while ((sample = reader.readSample()) != null) {
                writer.writeSample(sample);
                float progress = (float) ((sample.getTimestamp() - startTime) / (endTime - startTime));
                progressWindow.setProgress(progress);
                progressWindow.setStatus("Exporting data to " + file.getName() + " (" + DataViewer.formatDate(sample.getTimestamp()) + ")");
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
            writer.close();
        }
    }
