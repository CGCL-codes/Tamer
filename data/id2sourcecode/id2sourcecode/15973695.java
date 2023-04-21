        FileLock tryLock() throws IOException {
            File lockF = new File(root, STORAGE_FILE_LOCK);
            lockF.deleteOnExit();
            RandomAccessFile file = new RandomAccessFile(lockF, "rws");
            FileLock res = null;
            try {
                res = file.getChannel().tryLock();
            } catch (OverlappingFileLockException oe) {
                file.close();
                return null;
            } catch (IOException e) {
                LOG.info(StringUtils.stringifyException(e));
                file.close();
                throw e;
            }
            return res;
        }
