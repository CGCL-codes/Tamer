    private DedupFileChannel getFileChannel(String path, long handleNo) throws DokanOperationException {
        DedupFileChannel ch = this.dedupChannels.get(handleNo);
        if (ch == null) {
            File f = this.resolvePath(path);
            try {
                MetaDataDedupFile mf = MetaFileStore.getMF(f.getPath());
                ch = mf.getDedupFile().getChannel();
                channelLock.lock();
                try {
                    if (this.dedupChannels.containsKey(handleNo)) {
                        ch.close();
                        ch = this.dedupChannels.get(handleNo);
                    } else {
                        this.dedupChannels.put(handleNo, ch);
                    }
                } catch (Exception e) {
                } finally {
                    log.debug("number of channels is " + this.dedupChannels.size());
                    channelLock.unlock();
                }
            } catch (IOException e) {
                log.error("unable to open file" + f.getPath(), e);
                throw new DokanOperationException(WinError.ERROR_GEN_FAILURE);
            }
        }
        return ch;
    }
