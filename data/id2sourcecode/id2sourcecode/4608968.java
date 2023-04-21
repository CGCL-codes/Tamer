                                public void run() throws Exception {
                                    sem.acquire();
                                    synchronized ("mkdir") {
                                        FileUtils.forceMkdir(file.getParentFile());
                                    }
                                    FileUtils.deleteQuietly(file);
                                    FileUtils.copyFile(srcFile, file);
                                    logInfo(LOG, "restored file %s with copy of %s", fullFilename, srcFile.getName());
                                    if (consumer != null) {
                                        consumer.newResult(fullFilename);
                                    }
                                }
