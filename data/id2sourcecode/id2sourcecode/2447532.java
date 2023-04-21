    @Override
    @PartiallyCompleting
    public final void read(ReadRequest read) throws Exception {
        long lastReadOffset = read.getFileOffset() + read.getByteBuffer().capacity() - 1;
        if (!RAEendRepaired) {
            registryForAvailabilityNotification(ending());
            return;
        }
        if (lastReadOffset > super.ending()) {
            if (DEBUG_SEEKABLE_CHANNEL) LOGGER.log(Level.INFO, "buffer not filled yet req.={0}+>{1} avail={2}", new Object[] { read.getFileOffset(), read.getByteBuffer().capacity(), this });
            try {
                storeChannel = new RandomAccessFile(store, "rw").getChannel();
                storeChannel = storeChannel.position(this.ending() + 1);
                System.err.println("lastReadOffset > super.ending()    " + lastReadOffset + " > " + super.ending());
                System.err.println("read=" + read);
                startConnection(this.ending() + 1, (int) (read.getFileOffset() + read.getByteBuffer().capacity() - 1 - super.ending()), true);
            } catch (Exception a) {
                LOGGER.log(Level.INFO, "could not create new connection ", a);
            }
            return;
        }
        if (read.getFileOffset() - this.starting() < 0) {
            System.err.println("SeekableHttpChannel.java 312 : negative error=" + read.getFileOffset() + " " + this.starting());
        }
        int totalRead = 0;
        try {
            totalRead += storeChannel.read(read.getByteBuffer(), read.getFileOffset() - this.starting() + totalRead);
        } catch (Exception any) {
            LOGGER.log(Level.INFO, read.toString() + " this=" + this, any);
        }
        completedSuccessfully(read, this);
    }
