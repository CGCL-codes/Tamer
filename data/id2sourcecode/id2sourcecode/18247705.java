    @Override
    protected attrstat NFSPROC_WRITE_2(writeargs params) {
        final attrstat ret = new attrstat();
        ret.status = 0;
        final int count = params.data.length;
        final int offset = params.offset;
        try {
            final NFSFile f = pathManager.getNFSFileByHandle(params.file);
            if (logger.isDebugEnabled()) logger.debug("WRITE: " + f + " of " + count + " bytes");
            if (f.getFile().isDirectory()) {
                ret.status = nfsstat.NFSERR_ISDIR;
                return ret;
            }
            ret.attributes = f.getAttributes();
            f.flushCachedAttributes();
            try {
                final FileChannel c = f.getChannel(true);
                c.write(ByteBuffer.wrap(params.data, 0, count), offset);
            } catch (final SecurityException e) {
                logger.warn("WRITE: got exception for " + f, e);
                ret.status = nfsstat.NFSERR_ACCES;
            } catch (final IOException e) {
                if (logger.isInfoEnabled()) logger.info("WRITE: got exception for " + f, e);
                ret.status = nfsstat.NFSERR_IO;
            }
        } catch (final StaleHandleException e1) {
            logger.warn("WRITE: got stale handle");
            ret.status = nfsstat.NFSERR_STALE;
        } catch (final FileNotFoundException e) {
            ret.status = nfsstat.NFSERR_NOENT;
        }
        return ret;
    }
