    @Override
    protected attrstat NFSPROC_SETATTR_2(sattrargs params) {
        final attrstat ret = new attrstat();
        try {
            final NFSFile f = pathManager.getNFSFileByHandle(params.file);
            if (logger.isDebugEnabled()) logger.debug("SETATTR: " + f);
            ret.attributes = f.getAttributes();
            try {
                if (f.getAttributes().size != params.attributes.size && params.attributes.size != -1) try {
                    final FileChannel c = f.getChannel(true);
                    final long current = c.size();
                    if (current < params.attributes.size) {
                        c.position(params.attributes.size - 1);
                        c.write(ByteBuffer.allocate(1));
                    } else c.truncate(params.attributes.size);
                    f.flushCachedAttributes();
                } catch (final SecurityException e) {
                    logger.warn("READ: got exception for " + f, e);
                    ret.status = nfsstat.NFSERR_ACCES;
                } catch (final IOException e) {
                    logger.warn("READ: got exception for " + f, e);
                    ret.status = nfsstat.NFSERR_IO;
                }
            } catch (final SecurityException e) {
                logger.warn("SETATTR: got exception for " + f, e);
                ret.status = nfsstat.NFSERR_ACCES;
                return ret;
            }
        } catch (final StaleHandleException e1) {
            logger.warn("SETATTR: got stale handle");
            ret.status = nfsstat.NFSERR_STALE;
        } catch (final FileNotFoundException e) {
            ret.status = nfsstat.NFSERR_NOENT;
        }
        return ret;
    }
