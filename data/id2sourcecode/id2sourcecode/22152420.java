            @Override
            public void run() {
                try {
                    int count;
                    char[] buffer = new char[8 * 1024];
                    if (logger.isTraceEnabled()) logger.trace("Using reader " + reader + " for the stream transformation.");
                    while ((count = reader.read(buffer)) != -1) {
                        for (Writer writer : writers) {
                            if (logger.isTraceEnabled()) {
                                logger.trace("Read " + new String(buffer, 0, count) + " from reader " + reader + ", writing to writer " + writer);
                            }
                            writer.write(buffer, 0, count);
                        }
                    }
                    for (Writer writer : writers) writer.close();
                    logger.debug("Executable for the stream pipeline will now exit.");
                } catch (Throwable t) {
                    logger.error("Error during pipeline thread execution.", t);
                }
            }
