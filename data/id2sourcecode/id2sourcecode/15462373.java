    private void importDataThread(List<URL> dataFiles, List<String> sourceNames) throws IOException, RBNBException {
        for (int i = 0; i < dataFiles.size(); i++) {
            URL dataFile = dataFiles.get(i);
            String sourceName = sourceNames.get(i);
            progressWindow.setStatus("Importing data file " + getFileName(dataFile));
            float minProgress = (float) (i) / dataFiles.size();
            float maxProgress = (float) (i + 1) / dataFiles.size();
            DataFileReader reader = new DataFileReader(dataFile);
            if (reader.getProperty("samples") == null) {
                throw new IOException("Unable to determine the number of data samples.");
            }
            int samples = Integer.parseInt(reader.getProperty("samples"));
            int archiveSize = (int) Math.ceil((double) samples / SAMPLES_PER_FLUSH);
            RBNBController rbnb = RBNBController.getInstance();
            RBNBSource source = new RBNBSource(sourceName, archiveSize, rbnb.getRBNBHostName(), rbnb.getRBNBPortNumber());
            List<DataChannel> channels = reader.getChannels();
            for (DataChannel channel : channels) {
                source.addChannel(channel.getName(), "application/octet-stream", channel.getUnit());
            }
            int currentSample = 0;
            NumericDataSample sample;
            while ((sample = reader.readSample()) != null) {
                double timestamp = sample.getTimestamp();
                Number[] values = sample.getValues();
                for (int j = 0; j < values.length; j++) {
                    if (values[j] == null) {
                        continue;
                    }
                    source.putData(channels.get(j).getName(), timestamp, values[j].doubleValue());
                }
                currentSample++;
                if (currentSample % 50 == 0) {
                    source.flush();
                }
                if (samples != -1) {
                    progressWindow.setProgress(minProgress + maxProgress * currentSample / samples);
                }
            }
            source.flush();
            source.close();
            progressWindow.setProgress(maxProgress);
        }
    }
