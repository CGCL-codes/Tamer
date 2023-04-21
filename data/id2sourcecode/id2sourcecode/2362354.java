        public void run() {
            for (; ; ) {
                int size = journal.size();
                if (size > 0) {
                    logger.debug("To get the journal queue size " + journal.size());
                    Long[] queue = journal.get();
                    logger.debug("Writer is running with size " + queue.length);
                    for (long ver : queue) {
                        logger.debug("Read, from the journal, entry of Version " + ver);
                        logger.debug("Writing updates to disks");
                        File datafile = new File(jdataPath + File.separator + ver + ".dat");
                        File auxfile = new File(jdataPath + File.separator + ver + ".aux");
                        if (datafile.exists() == false || auxfile.exists() == false) {
                            logger.error("Version " + ver + " does not existed!");
                            continue;
                        }
                        long position = 0;
                        int length;
                        ByteBuffer buffer = null;
                        try {
                            RandomAccessFile posfile = new RandomAccessFile(auxfile, "r");
                            position = posfile.readLong();
                            length = posfile.readInt();
                            posfile.close();
                            logger.debug("Read from aux file " + position + ", " + length);
                            FileChannel wChannel = new FileInputStream(datafile).getChannel();
                            buffer = ByteBuffer.allocate(length);
                            wChannel.read(buffer);
                            wChannel.close();
                            logger.debug("Read from data file, bytes size " + buffer.capacity());
                        } catch (IOException e) {
                            logger.error(Util.getStackTrace(e));
                        }
                        byte[] bytes;
                        if (buffer != null) {
                            bytes = buffer.array();
                            if (writeTask(bytes, position, ver)) {
                                if (datafile.delete() == false || auxfile.delete() == false) {
                                    logger.error("Version " + ver + " could not be removed!");
                                } else {
                                    logger.debug("Removed Version " + ver);
                                }
                                journal.remove(ver);
                                logger.debug("Remove, from the journal, entry of Version " + ver);
                                logger.debug("The journal (size " + journal.size() + ")");
                            } else {
                                logger.debug("Not all replicas written, keep the journal");
                            }
                        }
                    }
                } else {
                    try {
                        Thread.sleep(0, 100);
                    } catch (InterruptedException e) {
                        logger.error(Util.getStackTrace(e));
                    }
                }
            }
        }
