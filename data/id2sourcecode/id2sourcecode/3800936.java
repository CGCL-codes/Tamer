                    @Override
                    public void responseAvailable(ReusableBuffer buffer) {
                        synchronized (openChunks) {
                            FileChannel fChannel = null;
                            try {
                                if (openChunks.get() < 0) throw new IOException();
                                if (buffer.remaining() == 0) {
                                    Logging.logMessage(Logging.LEVEL_WARN, this, "LOAD: CHUNK ERROR: Empty buffer received!");
                                    throw new IOException("CHUNK ERROR: Empty buffer received!");
                                }
                                File f = fileIO.getFile(fileName);
                                Logging.logMessage(Logging.LEVEL_DEBUG, this, "LOAD: SAVING %s to %s.", fileName, f.getPath());
                                assert (f.exists()) : "File '" + fileName + "' was not created properly.";
                                fChannel = new FileOutputStream(f).getChannel();
                                fChannel.write(buffer.getBuffer(), pos1);
                            } catch (IOException e) {
                                Logging.logMessage(Logging.LEVEL_WARN, this, "LOAD: Chunk request (%s,%d,%d) failed: %s", fileName, pos1, size, e.getMessage());
                                openChunks.set(-1);
                                openChunks.notify();
                                return;
                            } finally {
                                if (fChannel != null) {
                                    try {
                                        fChannel.close();
                                    } catch (IOException e) {
                                        Logging.logError(Logging.LEVEL_ERROR, this, e);
                                    }
                                }
                                if (buffer != null) BufferPool.free(buffer);
                            }
                            if (openChunks.get() != -1 && openChunks.decrementAndGet() == 0) openChunks.notify();
                        }
                    }
