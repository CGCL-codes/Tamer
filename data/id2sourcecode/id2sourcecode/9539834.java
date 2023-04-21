    public static void acquireLock(String id, MessageHandler messageHandler) throws AlreadyLockedException {
        File lockFile;
        File portFile;
        FileChannel fileChannel;
        FileLock fileLock;
        Server server;
        String nid = normalizeID(id);
        j_lock();
        try {
            lockFile = getLockFileForNID(nid);
            portFile = getPortFileForNID(nid);
            LOCK_FILES_DIR.mkdirs();
            try {
                RandomAccessFile raf = new RandomAccessFile(lockFile, "rw");
                fileChannel = raf.getChannel();
                fileLock = fileChannel.tryLock();
                if (fileLock == null) {
                    throw new AlreadyLockedException(id);
                }
            } catch (Throwable t) {
                throw new AlreadyLockedException(id);
            }
            server = new Server(id, messageHandler);
            Lock lock = new Lock(id, lockFile, portFile, fileChannel, fileLock, server);
            locks.put(nid, lock);
            server.start();
            Writer portWriter = null;
            try {
                portWriter = new FileWriter(portFile);
                portWriter.write(String.valueOf(server.getListenedPort()));
                portWriter.flush();
            } catch (Throwable t) {
                ;
            } finally {
                if (portWriter != null) {
                    try {
                        portWriter.close();
                    } catch (Throwable t) {
                        ;
                    }
                }
            }
        } finally {
            j_unlock();
        }
    }
