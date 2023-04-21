    public void force(Set<Long> fileNums) {
        for (long fileNum : fileNums) {
            RandomAccessFile file = null;
            try {
                FileHandle handle = makeFileHandle(fileNum, getAppropriateReadWriteMode());
                file = handle.getFile();
                file.getChannel().force(false);
                nLogFSyncs.increment();
            } catch (FileNotFoundException e) {
                throw new EnvironmentFailureException(envImpl, EnvironmentFailureReason.LOG_FILE_NOT_FOUND, "Invisible fsyncing file " + fileNum, e);
            } catch (ChecksumException e) {
                throw new EnvironmentFailureException(envImpl, EnvironmentFailureReason.LOG_CHECKSUM, "Invisible fsyncing file " + fileNum, e);
            } catch (IOException e) {
                throw new EnvironmentFailureException(envImpl, EnvironmentFailureReason.LOG_WRITE, "Invisible fsyncing file " + fileNum, e);
            } finally {
                if (file != null) {
                    try {
                        file.close();
                    } catch (IOException e) {
                        throw new EnvironmentFailureException(envImpl, EnvironmentFailureReason.LOG_WRITE, "Invisible fsyncing file " + fileNum, e);
                    }
                }
            }
        }
    }
