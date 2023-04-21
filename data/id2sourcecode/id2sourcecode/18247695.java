    @Override
    protected readres NFSPROC_READ_2(readargs params) {
        final readres ret = new readres();
        try {
            final NFSFile f = pathManager.getNFSFileByHandle(params.file);
            final int offset = params.offset;
            final int count = Math.min(nfs_prot.NFS_MAXDATA, params.count);
            ret.reply = new readokres();
            ret.reply.data = new byte[count];
            if (logger.isDebugEnabled()) logger.debug("READ: " + f + "," + count + " bytes " + offset + " offset");
            ret.reply.attributes = f.getAttributes();
            if (f.getFile().isDirectory()) {
                ret.status = nfsstat.NFSERR_ISDIR;
                if (logger.isInfoEnabled()) logger.info("READ: attempt to read from drectory");
                return ret;
            }
            try {
                final FileChannel c = f.getChannel(false);
                final ByteBuffer b = ByteBuffer.wrap(ret.reply.data);
                int total = 0;
                int read = 0;
                do {
                    read = c.read(b, offset + total);
                    total += read;
                } while (read > 0 && total < count);
            } catch (final SecurityException e) {
                logger.warn("READ: got exception for " + f, e);
                ret.status = nfsstat.NFSERR_ACCES;
            } catch (final IOException e) {
                logger.warn("READ: got exception for " + f, e);
                ret.status = nfsstat.NFSERR_IO;
            }
        } catch (final StaleHandleException e1) {
            logger.warn("READ: Got stale handle");
            ret.status = nfsstat.NFSERR_STALE;
        } catch (final FileNotFoundException e) {
            if (logger.isInfoEnabled()) logger.info("READ: the file has vanished.", e);
            ret.status = nfsstat.NFSERR_NOENT;
        }
        return ret;
    }
