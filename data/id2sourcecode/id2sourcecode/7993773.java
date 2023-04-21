                @Override
                public JobStatus run() {
                    try {
                        FileOutputStream fileOutputStream = new FileOutputStream(filePath);
                        InputStream source = new ByteArrayInputStream(player.getMusicBytes());
                        byte buffer[] = new byte[1024];
                        int read;
                        do {
                            read = source.read(buffer);
                            if (read > 0) {
                                fileOutputStream.write(buffer, 0, read);
                            }
                        } while (read != -1);
                        fileOutputStream.close();
                        source.close();
                    } catch (IOException e) {
                        GroofyLogger.getInstance().logError(e.getLocalizedMessage());
                    }
                    return JobStatus.OK;
                }
