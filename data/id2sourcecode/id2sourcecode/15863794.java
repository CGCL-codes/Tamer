            @Override
            public void run() {
                try {
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        String transformedLine = transformLine(line);
                        if (logger.isTraceEnabled()) logger.trace("Read '" + line + "' from reader" + bufferedReader + ", writing '" + transformedLine + "' to writer " + writer);
                        writer.write(transformedLine);
                    }
                    String transformedLine = transformLine(line);
                    logger.trace("Writing final '" + transformedLine + "' to writer " + writer);
                    writer.write(transformedLine);
                    writer.close();
                    logger.debug("Executable for the line reader pipeline will now exit.");
                } catch (Throwable t) {
                    logger.error("Error during pipeline thread execution.", t);
                }
            }
