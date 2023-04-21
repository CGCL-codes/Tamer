    public void run() {
        long indexPart;
        int nbByteSendInSecond = 0;
        int nbByteRead = 0;
        int nbByteWritten = 0;
        int bufferLimit = 0;
        long startUpload = 0;
        long nbPart = fileToUpload.getSize() / packetSize;
        try {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(true);
            serverSocketChannel.socket().bind(new InetSocketAddress(port));
            fileChannel = new FileInputStream(fileToUpload.getPath()).getChannel();
            socketChannel = serverSocketChannel.accept();
            alive = true;
            while ((indexPart = getIndexPart()) > -1 && alive) {
                System.out.println("\n-------------- New Part " + indexPart + " ----------------");
                nbByteRead = readPart(indexPart);
                System.out.println("nb byte read : " + nbByteRead);
                bufferLimit = 0;
                while (buffer.position() < nbByteRead) {
                    bufferLimit = bufferLimit + (nbByteSendPerSecond - nbByteSendInSecond);
                    if (bufferLimit > nbByteRead) bufferLimit = nbByteRead;
                    System.out.println("limit buffer : " + bufferLimit);
                    buffer.limit(bufferLimit);
                    if (startUpload == 0) startUpload = System.currentTimeMillis();
                    while (buffer.hasRemaining()) {
                        nbByteWritten = socketChannel.write(buffer);
                        nbByteSendInSecond += nbByteWritten;
                        totalByteWritten += nbByteWritten;
                        System.out.println("byte envoye dans la seconde : " + nbByteSendInSecond + "\ntotal : " + totalByteWritten);
                    }
                    if (nbByteSendInSecond >= nbByteSendPerSecond) {
                        long time = System.currentTimeMillis() - startUpload;
                        long sleep = 1000 - time;
                        if (sleep > 0) {
                            try {
                                Thread.sleep(sleep);
                                System.out.println("Je viens de dormir : " + sleep + " ms");
                            } catch (InterruptedException ie) {
                                ie.printStackTrace();
                            }
                        }
                        if (time > 0) nbByteSendPerSecond = (int) ((nbByteSendInSecond * 1000) / time); else nbByteSendPerSecond *= 10;
                        System.out.println("nouveau taux : " + nbByteSendPerSecond);
                        startUpload = 0;
                        nbByteSendInSecond = 0;
                    }
                }
            }
            socketChannel.close();
            serverSocketChannel.close();
            System.out.println("FIN du transfert!!");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
