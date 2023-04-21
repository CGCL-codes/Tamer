    private void flushBuffer(boolean lock) throws IOException {
        List<ChunkData> oldkBuf = null;
        if (lock) {
            this.arlock.lock();
        }
        try {
            if (kBuf.size() == 0) {
                return;
            }
            oldkBuf = kBuf;
            if (this.isClosed()) kBuf = null; else {
                kBuf = new ArrayList<ChunkData>(oldkBuf.size());
            }
        } finally {
            if (lock) this.arlock.unlock();
        }
        if (oldkBuf.size() > 0) {
            Iterator<ChunkData> iter = oldkBuf.iterator();
            this.iolock.lock();
            this.flushing = true;
            try {
                while (iter.hasNext()) {
                    ChunkData cm = iter.next();
                    if (cm != null) {
                        long pos = (cm.getcPos() / (long) Main.CHUNK_LENGTH) * (long) ChunkData.RAWDL;
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
                    }
                }
                kFc.force(true);
            } catch (Exception e) {
                SDFSLogger.getLog().error("error while flushing buffers", e);
            } finally {
                this.flushing = false;
                this.iolock.unlock();
            }
            oldkBuf.clear();
        }
        oldkBuf = null;
    }
