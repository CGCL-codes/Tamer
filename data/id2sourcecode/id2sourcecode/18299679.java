    @Override
    public void importLogs(InputStream in, LogDataCollector dataCollector, ParsingContext parsingContext) {
        HashMap<String, Object> customConextProperties = parsingContext.getCustomConextProperties();
        if (customConextProperties.containsKey(PROPERTY_LOG_IMPORTER)) {
            LogImporter logImporter = (LogImporter) customConextProperties.get(PROPERTY_LOG_IMPORTER);
            LOGGER.fine(String.format("Have log imporer detected (%s), will use it", logImporter.getName()));
            logImporter.importLogs(in, dataCollector, parsingContext);
        } else {
            try {
                byte[] buff = new byte[16 * 1024];
                int read = 0;
                while ((read = in.read(buff)) > 0) {
                    ByteArrayOutputStream byteArrayOutputStream = (java.io.ByteArrayOutputStream) customConextProperties.get(PROPERTY_BYTE_BUFFER);
                    int totalRead = byteArrayOutputStream.size();
                    totalRead += read;
                    if (totalRead < detectTryMinimum) {
                        LOGGER.fine(String.format("To small amout of data to detect log imporerter [%db]", totalRead));
                        byteArrayOutputStream.write(buff, 0, read);
                    } else if (totalRead > detectTryMaximum) {
                        parsingContext.setParsingInProgress(false);
                        LOGGER.warning("Reached maximum size of log imporetr deteciton buffer, Will not load more data");
                    } else {
                        byteArrayOutputStream.write(buff, 0, read);
                        LOGGER.fine("Trying to detect log importer");
                        LogImporter detectLogImporter = Utils.detectLogImporter(logImporters, byteArrayOutputStream.toByteArray());
                        if (detectLogImporter != null) {
                            LOGGER.fine(String.format("Log imporer detected (%s),this log importer will be used", detectLogImporter.getName()));
                            detectLogImporter.initParsingContext(parsingContext);
                            customConextProperties.put(PROPERTY_LOG_IMPORTER, detectLogImporter);
                            byte[] buf = byteArrayOutputStream.toByteArray();
                            SequenceInputStream sequenceInputStream = new SequenceInputStream(new ByteArrayInputStream(buf), in);
                            detectLogImporter.importLogs(sequenceInputStream, dataCollector, parsingContext);
                            return;
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                LOGGER.warning("IOException reading log file " + parsingContext.getLogSource());
            }
        }
    }
