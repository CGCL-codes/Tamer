    private void serveDoWrite(String pathname, DataInputStream in, DataOutputStream out) throws IOException {
        RFile file = new RFile(pathname);
        RFile ancestor = file.getRootFile();
        DirectoryConfiguration conf = server.getDirectoryDefaultConfiguration(ancestor.getName());
        String command = in.readUTF();
        boolean transaction = conf.getDefaultWriteMode() == WriteMode.TRANSACTED;
        boolean append = false;
        boolean allowoverwrite = true;
        int minpeers = conf.getMinPeers();
        while (!command.equals(Requests.STARTSTREAM)) {
            if (command.equals(Requests.APPEND)) {
                append = true;
            }
            if (command.equals(Requests.TRANSACTION)) {
                transaction = true;
            }
            if (command.equals(Requests.NOOVERWRITE)) {
                allowoverwrite = false;
            }
            if (command.equals(Requests.REPLICATE)) {
                minpeers = Integer.parseInt(in.readUTF());
            }
            command = in.readUTF();
        }
        if (append && transaction) {
            sendError("transaction cannot be done in APPEND mode", out);
            return;
        }
        logger.log(Level.FINE, "pathname={0} minpeers={1}", new Object[] { pathname, minpeers });
        String origpathname = pathname;
        File originalFile = fileManager.getRawFile(pathname);
        boolean swallow = false;
        if (!allowoverwrite && originalFile.isFile()) {
            System.out.println("File already exists " + originalFile.getAbsolutePath());
            swallow = true;
        }
        if (conf.isCachable()) {
            server.getCache().removeFromCache(pathname);
            server.getLocalClient().fileChanged(pathname);
        }
        originalFile.getParentFile().mkdirs();
        File tmpFile = originalFile;
        if (transaction) {
            pathname = pathname + ".part";
            tmpFile = fileManager.getRawFile(pathname);
            tmpFile.getParentFile().mkdirs();
        }
        int count = 0;
        FileOutputStream outfile = null;
        WritableByteChannel outC = null;
        try {
            if (!swallow) {
                outfile = fileManager.openFileWriter(pathname, append);
                outC = outfile.getChannel();
            }
            while (true) {
                command = in.readUTF();
                logger.log(Level.FINEST, "received command {0}", new Object[] { command });
                if (command.equals(Requests.END_OF_FILE)) {
                    break;
                } else if (command.equals(Requests.WRITE_SINGLE_BYTE)) {
                    int b = in.read();
                    logger.log(Level.FINEST, "receiving one single byte");
                    ByteBuffer buffer = ByteBuffer.allocateDirect(1);
                    buffer.put((byte) b);
                    buffer.rewind();
                    if (!swallow) {
                        outC.write(buffer);
                    }
                    count++;
                } else if (command.startsWith(Requests.WRITE_BYTE_ARRAY)) {
                    int len = Integer.parseInt(command.substring(1));
                    logger.log(Level.FINEST, "receiving {0} bytes", len);
                    ByteBuffer buffer = ByteBuffer.allocate(len);
                    int actual = 1;
                    int single = in.read();
                    buffer.put((byte) single);
                    while (actual < len) {
                        if (single < 0) {
                            throw new IOException("unexpected EOF");
                        }
                        single = in.read();
                        buffer.put((byte) single);
                        actual++;
                    }
                    logger.log(Level.FINEST, "receiving {0} bytes got {1}", new Object[] { len, actual });
                    if (actual != len) {
                        throw new IOException("expected " + len + " bytes but got " + actual);
                    }
                    buffer.rewind();
                    if (!swallow) {
                        outC.write(buffer);
                    }
                    count += len;
                }
            }
            if (swallow) {
                sendError("file already exists", out);
                return;
            }
            outfile.close();
            outfile = null;
            logger.log(Level.FINE, "pathname={0} received total {1} bytes", new Object[] { pathname, count });
            if (transaction) {
                logger.log(Level.FINE, "committing transaction");
                if (originalFile.isFile()) {
                    originalFile.delete();
                }
                tmpFile.renameTo(originalFile);
                if (tmpFile.exists() || !originalFile.exists()) {
                    throw new IOException("commit failed of file " + origpathname + " (file is " + originalFile.getAbsolutePath() + " tmp is " + tmpFile.getAbsolutePath() + ")");
                }
            }
            if (minpeers > 1) {
                logger.log(Level.FINE, "min peers is {0} so replicate file on other peers", minpeers);
                try {
                    this.server.getReplicaManager().replicate(origpathname, minpeers, true);
                } catch (IOException err) {
                    sendError("replication error " + err, out);
                    return;
                }
            }
            out.write(Responses.OK);
            out.flush();
            if (conf.isCachable()) {
                try {
                    server.getCache().cache(pathname, originalFile);
                } catch (IOException err) {
                    logger.log(Level.SEVERE, "error while caching file " + originalFile + ":{0}", err);
                }
            }
        } catch (IOException err) {
            logger.log(Level.INFO, "pathname={0} received {1} bytes but got {2}", new Object[] { pathname, count, err });
            sendError("server error " + err, out);
            if (transaction) {
                if (outfile != null) {
                    outfile.close();
                }
                outfile = null;
                logger.log(Level.INFO, "transaction failed! deleting tmp file");
                fileManager.getRawFile(pathname).delete();
            }
        } finally {
            if (outfile != null) {
                outfile.close();
            }
        }
    }
