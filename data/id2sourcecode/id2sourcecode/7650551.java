    public boolean remove(ChunkData cm, boolean persist) throws IOException {
        if (this.isClosed()) {
            throw new IOException("hashtable [" + this.fileName + "] is close");
        }
        try {
            if (cm.getHash().length == 0) return true;
            if (!this.getMap(cm.getHash()).remove(cm.getHash())) {
                cm.setmDelete(false);
                return false;
            } else {
                cm.setmDelete(true);
                if (persist) {
                    this.iolock.lock();
                    if (this.isClosed()) {
                        throw new IOException("hashtable [" + this.fileName + "] is close");
                    }
                    try {
                        long pos = (cm.getcPos() / Main.CHUNK_LENGTH) * (long) ChunkData.RAWDL;
                        if (cm.ismDelete()) cm.setLastClaimed(0); else {
                            cm.setLastClaimed(System.currentTimeMillis());
                        }
                        try {
                            kFc.write(cm.getMetaDataBytes(), pos);
                        } catch (java.nio.channels.ClosedChannelException e1) {
                            kFc = (FileChannelImpl) new RandomAccessFile(fileName, this.fileParams).getChannel();
                            kFc.write(cm.getMetaDataBytes(), pos);
                        } catch (Exception e) {
                            SDFSLogger.getLog().error("error while writing buffer", e);
                        } finally {
                            if (cm.ismDelete()) {
                                this.addFreeSlot(cm.getcPos());
                                this.kSz--;
                            }
                            cm = null;
                        }
                        cm = null;
                    } catch (Exception e) {
                    } finally {
                        this.iolock.unlock();
                    }
                }
                return true;
            }
        } catch (Exception e) {
            SDFSLogger.getLog().fatal("error getting record", e);
            return false;
        }
    }
