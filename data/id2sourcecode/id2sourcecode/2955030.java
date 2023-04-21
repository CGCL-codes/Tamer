    @SuppressWarnings({ "ResultOfMethodCallIgnored" })
    public static boolean saveBuffer(ByteBuffer buffer, File file, boolean forceFilesystemWrite) throws IOException {
        if (buffer == null) {
            String message = Logging.getMessage("nullValue.BufferNull");
            Logging.logger().severe(message);
            throw new IllegalArgumentException(message);
        }
        if (file == null) {
            String message = Logging.getMessage("nullValue.FileIsNull");
            Logging.logger().severe(message);
            throw new IllegalArgumentException(message);
        }
        FileOutputStream fos = null;
        FileChannel channel = null;
        FileLock lock;
        int numBytesWritten = 0;
        try {
            fos = new FileOutputStream(file);
            channel = fos.getChannel();
            lock = channel.tryLock();
            if (lock == null) {
                Logging.logger().log(Level.FINER, "WWIO.UnableToAcquireLockFor", file.getPath());
                return false;
            }
            for (buffer.rewind(); buffer.hasRemaining(); ) {
                numBytesWritten += channel.write(buffer);
            }
            if (forceFilesystemWrite) channel.force(true);
            fos.flush();
            return true;
        } catch (ClosedByInterruptException e) {
            Logging.logger().log(Level.FINE, Logging.getMessage("generic.interrupted", "WWIO.saveBuffer", file.getPath()), e);
            if (numBytesWritten > 0) file.delete();
            throw e;
        } catch (IOException e) {
            Logging.logger().log(Level.SEVERE, Logging.getMessage("WWIO.ErrorSavingBufferTo", file.getPath()), e);
            if (numBytesWritten > 0) file.delete();
            throw e;
        } finally {
            WWIO.closeStream(channel, file.getPath());
            WWIO.closeStream(fos, file.getPath());
        }
    }
