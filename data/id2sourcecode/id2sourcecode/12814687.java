    public final void handleRequest(@Nonnull final SocketChannel inChannel, @Nonnull final Socket socket) throws IOException {
        final StopWatch sw = new StopWatch();
        if (!inChannel.isBlocking()) {
            inChannel.configureBlocking(true);
        }
        InputStream in = socket.getInputStream();
        DataInputStream dis = new DataInputStream(in);
        String fname = IOUtils.readString(dis);
        String dirPath = IOUtils.readString(dis);
        long len = dis.readLong();
        boolean append = dis.readBoolean();
        boolean ackRequired = dis.readBoolean();
        boolean hasAdditionalHeader = dis.readBoolean();
        if (hasAdditionalHeader) {
            readAdditionalHeader(dis, fname, dirPath, len, append, ackRequired);
        }
        final File file;
        if (dirPath == null) {
            file = new File(baseDir, fname);
        } else {
            File dir = FileUtils.resolvePath(baseDir, dirPath);
            file = new File(dir, fname);
        }
        preFileAppend(file, append);
        final FileOutputStream dst = new FileOutputStream(file, append);
        final String fp = file.getAbsolutePath();
        final ReadWriteLock filelock = accquireLock(fp, locks);
        final FileChannel fileCh = dst.getChannel();
        final long startPos = file.length();
        try {
            NIOUtils.transferFullyFrom(inChannel, 0, len, fileCh);
        } finally {
            IOUtils.closeQuietly(fileCh, dst);
            releaseLock(fp, filelock, locks);
            postFileAppend(file, startPos, len);
        }
        if (ackRequired) {
            OutputStream out = socket.getOutputStream();
            DataOutputStream dos = new DataOutputStream(out);
            dos.writeLong(len);
            postAck(file, startPos, len);
        }
        if (LOG.isDebugEnabled()) {
            SocketAddress remoteAddr = socket.getRemoteSocketAddress();
            LOG.debug("Received a " + (append ? "part of file '" : "file '") + file.getAbsolutePath() + "' of " + len + " bytes from " + remoteAddr + " in " + sw.toString());
        }
    }
