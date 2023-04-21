    public boolean write(byte[] bytes, long position) {
        logger.debug("Write to DISK " + bytes.length + " bytes at " + position);
        long ver = version.increment();
        logger.debug("Start journaling for update version " + ver);
        try {
            File datafile = new File(jdataPath + File.separator + ver + ".dat");
            File auxfile = new File(jdataPath + File.separator + ver + ".aux");
            if (datafile.exists() == true || auxfile.exists() == true) {
                logger.error("Version " + ver + " existed!");
                return false;
            }
            logger.debug("Writing data to the data file (size " + datafile.length() + ")");
            FileChannel wChannel = new FileOutputStream(datafile, false).getChannel();
            wChannel.write(ByteBuffer.wrap(bytes));
            wChannel.close();
            logger.debug("data file size is " + datafile.length());
            logger.debug("Writing data to the aux file (size " + auxfile.length() + ")");
            RandomAccessFile posfile = new RandomAccessFile(auxfile, "rw");
            posfile.writeLong(position);
            posfile.writeInt(bytes.length);
            posfile.close();
            logger.debug("Writing data to the aux file (size " + auxfile.length() + ")");
            int virtualChunk = (int) Math.floor(position / this.volume.getChunkSize());
            int numBytesLeft = bytes.length;
            int count = 0;
            long cursor = (position - virtualChunk * this.volume.getChunkSize());
            while (numBytesLeft > 0) {
                virtualChunkVersion[virtualChunk].setVersionNumber(ver);
                long numBytesAdjusted = numBytesLeft;
                if ((cursor + numBytesLeft) > this.volume.getChunkSize()) {
                    numBytesAdjusted = this.volume.getChunkSize() - cursor;
                }
                count += numBytesAdjusted;
                numBytesLeft -= numBytesAdjusted;
                if (numBytesLeft > 0) {
                    virtualChunk++;
                    cursor = 0;
                }
            }
        } catch (IOException e) {
            logger.error(Util.getStackTrace(e));
        }
        journal.add(new Long(ver));
        logger.debug("journal size " + journal.size());
        logger.debug("End journaling for update version " + ver);
        return true;
    }
