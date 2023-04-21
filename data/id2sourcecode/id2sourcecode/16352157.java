    private void readServerMet() throws NestableException {
        FileInputStream serverMet = null;
        serverList = new ArrayList<Ed2kServer>();
        try {
            serverMet = new FileInputStream(this.serverMet);
        } catch (FileNotFoundException e) {
            logger.error("Server met file not found", e);
        }
        FileChannel fileReader = serverMet.getChannel();
        ByteBuffer fileBuffer = ByteBuffer.allocate((int) this.serverMet.length());
        try {
            fileReader.read(fileBuffer);
            IoBuffer realBuffer = IoBuffer.wrap(fileBuffer);
            realBuffer.order(ByteOrder.LITTLE_ENDIAN);
            realBuffer.rewind();
            short header = realBuffer.getUnsigned();
            if (header != SERVER_MET_HEADER) {
                throw new NestableException("Server met corrupt, header found: " + header);
            }
            long numberOfServers = realBuffer.getUnsignedInt();
            if (logger.isDebugEnabled()) {
                logger.debug("Number of servers to parse: " + numberOfServers);
            }
            for (int i = 0; i < numberOfServers; i++) {
                Ed2kServer server = Ed2kServer.readServer(realBuffer, i);
                if (logger.isDebugEnabled()) {
                    logger.debug("Server parsed: " + server);
                }
                serverList.add(server);
            }
        } catch (IOException e) {
            logger.error("Error reading server met file", e);
            throw new NestableException(e);
        }
    }
