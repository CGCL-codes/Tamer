    public static void sendfile(@Nonnull final File file, final long fromPos, final long count, @Nullable final String writeDirPath, @Nonnull final InetAddress dstAddr, final int dstPort, final boolean append, final boolean sync, @Nonnull final TransferClientHandler handler) throws IOException {
        if (!file.exists()) {
            throw new IllegalArgumentException(file.getAbsolutePath() + " does not exist");
        }
        if (!file.isFile()) {
            throw new IllegalArgumentException(file.getAbsolutePath() + " is not file");
        }
        if (!file.canRead()) {
            throw new IllegalArgumentException(file.getAbsolutePath() + " cannot read");
        }
        final SocketAddress dstSockAddr = new InetSocketAddress(dstAddr, dstPort);
        SocketChannel channel = null;
        Socket socket = null;
        final OutputStream out;
        try {
            channel = SocketChannel.open();
            socket = channel.socket();
            socket.connect(dstSockAddr);
            out = socket.getOutputStream();
        } catch (IOException e) {
            LOG.error("failed to connect: " + dstSockAddr, e);
            IOUtils.closeQuietly(channel);
            NetUtils.closeQuietly(socket);
            throw e;
        }
        DataInputStream din = null;
        if (sync) {
            InputStream in = socket.getInputStream();
            din = new DataInputStream(in);
        }
        final DataOutputStream dos = new DataOutputStream(out);
        final StopWatch sw = new StopWatch();
        FileInputStream src = null;
        final long nbytes;
        try {
            src = new FileInputStream(file);
            FileChannel fc = src.getChannel();
            String fileName = file.getName();
            IOUtils.writeString(fileName, dos);
            IOUtils.writeString(writeDirPath, dos);
            long xferBytes = (count == -1L) ? fc.size() : count;
            dos.writeLong(xferBytes);
            dos.writeBoolean(append);
            dos.writeBoolean(sync);
            if (handler == null) {
                dos.writeBoolean(false);
            } else {
                dos.writeBoolean(true);
                handler.writeAdditionalHeader(dos);
            }
            nbytes = fc.transferTo(fromPos, xferBytes, channel);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Sent a file '" + file.getAbsolutePath() + "' of " + nbytes + " bytes to " + dstSockAddr.toString() + " in " + sw.toString());
            }
            if (sync) {
                long remoteRecieved = din.readLong();
                if (remoteRecieved != xferBytes) {
                    throw new IllegalStateException("Sent " + xferBytes + " bytes, but remote node received " + remoteRecieved + " bytes");
                }
            }
        } catch (FileNotFoundException e) {
            LOG.error(PrintUtils.prettyPrintStackTrace(e, -1));
            throw e;
        } catch (IOException e) {
            LOG.error(PrintUtils.prettyPrintStackTrace(e, -1));
            throw e;
        } finally {
            IOUtils.closeQuietly(src);
            IOUtils.closeQuietly(din, dos);
            IOUtils.closeQuietly(channel);
            NetUtils.closeQuietly(socket);
        }
    }
