    protected void writeBufferToFile(File file, ByteBuffer headerBuffer, byte[] bodyByteBuffer, int padding, int sizeIncPadding, long audioStartLocation) throws IOException {
        FileChannel fc = null;
        FileLock fileLock = null;
        if (sizeIncPadding > audioStartLocation) {
            logger.finest("Adjusting Padding");
            adjustPadding(file, sizeIncPadding, audioStartLocation);
        }
        try {
            fc = new RandomAccessFile(file, "rws").getChannel();
            fileLock = getFileLockForWriting(fc, file.getPath());
            fc.write(headerBuffer);
            fc.write(ByteBuffer.wrap(bodyByteBuffer));
            fc.write(ByteBuffer.wrap(new byte[padding]));
        } catch (FileNotFoundException fe) {
            logger.log(Level.SEVERE, getLoggingFilename() + fe.getMessage(), fe);
            if (fe.getMessage().equals(FileSystemMessage.ACCESS_IS_DENIED.getMsg())) {
                logger.severe(ErrorMessage.GENERAL_WRITE_FAILED_TO_OPEN_FILE_FOR_EDITING.getMsg(file.getPath()));
                throw new UnableToModifyFileException(ErrorMessage.GENERAL_WRITE_FAILED_TO_OPEN_FILE_FOR_EDITING.getMsg(file.getPath()));
            } else {
                logger.severe(ErrorMessage.GENERAL_WRITE_FAILED_TO_OPEN_FILE_FOR_EDITING.getMsg(file.getPath()));
                throw new UnableToCreateFileException(ErrorMessage.GENERAL_WRITE_FAILED_TO_OPEN_FILE_FOR_EDITING.getMsg(file.getPath()));
            }
        } catch (IOException ioe) {
            logger.log(Level.SEVERE, getLoggingFilename() + ioe.getMessage(), ioe);
            if (ioe.getMessage().equals(FileSystemMessage.ACCESS_IS_DENIED.getMsg())) {
                logger.severe(ErrorMessage.GENERAL_WRITE_FAILED_TO_OPEN_FILE_FOR_EDITING.getMsg(file.getParentFile().getPath()));
                throw new UnableToModifyFileException(ErrorMessage.GENERAL_WRITE_FAILED_TO_OPEN_FILE_FOR_EDITING.getMsg(file.getParentFile().getPath()));
            } else {
                logger.severe(ErrorMessage.GENERAL_WRITE_FAILED_TO_OPEN_FILE_FOR_EDITING.getMsg(file.getParentFile().getPath()));
                throw new UnableToCreateFileException(ErrorMessage.GENERAL_WRITE_FAILED_TO_OPEN_FILE_FOR_EDITING.getMsg(file.getParentFile().getPath()));
            }
        } finally {
            if (fc != null) {
                if (fileLock != null) {
                    fileLock.release();
                }
                fc.close();
            }
        }
    }
