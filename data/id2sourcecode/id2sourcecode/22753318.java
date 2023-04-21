    public void run() {
        int nbByteRead = 0;
        long indexPacketToDownload = 0;
        int nbBytesInSecond = 0;
        int bufferLimit;
        long startDownload = 0;
        try {
            FileChannel fileChannel = new FileOutputStream(downloadingFile).getChannel();
            socketChannel = SocketChannel.open(new InetSocketAddress(message.getIPSender(), message.getUploadPort()));
            socketChannel.configureBlocking(true);
            PreparedStatement pstmt = sqlConn.prepareStatement("UPDATE downloading SET nb_bytes_downloaded=?");
            while (!isDownloadFinish()) {
                indexPacketToDownload = getNextPart();
                System.out.println("\n\n-------------------------------------\nJe demande la partie : " + indexPacketToDownload);
                askIndexPart(indexPacketToDownload);
                buffer.clear();
                nbByteRead = 0;
                bufferLimit = 0;
                while (buffer.position() < buffer.capacity() && nbByteRead > -1 && totalNbByteRead < message.getFileSize()) {
                    bufferLimit = bufferLimit + (nbBytesPerSecond - nbBytesInSecond);
                    if (bufferLimit > buffer.capacity()) bufferLimit = buffer.capacity();
                    buffer.limit(bufferLimit);
                    if (startDownload == 0) startDownload = System.currentTimeMillis();
                    nbByteRead = 0;
                    while (buffer.hasRemaining() && nbByteRead > -1 && totalNbByteRead < message.getFileSize()) {
                        nbByteRead = socketChannel.read(buffer);
                        if (nbByteRead > -1) {
                            totalNbByteRead += nbByteRead;
                            nbBytesInSecond += nbByteRead;
                        }
                        System.out.println("J'ai lu nb bytes : " + nbByteRead);
                    }
                    System.out.println("nbBytesInSecond : " + nbBytesInSecond + " & nbBytesPerSecond : " + nbBytesPerSecond);
                    if (nbBytesInSecond >= nbBytesPerSecond) {
                        long time = System.currentTimeMillis() - startDownload;
                        long sleep = 1000 - time;
                        if (sleep > 0) {
                            try {
                                Thread.sleep(sleep);
                                System.out.println("Je viens de dormir : " + sleep + " ms");
                            } catch (InterruptedException ie) {
                                ie.printStackTrace();
                            }
                        } else System.out.println("J'ai mis trop de temps : " + Math.abs(sleep));
                        if (time > 0) nbBytesPerSecond = (int) ((nbBytesInSecond * 1000) / time); else nbBytesPerSecond *= 10;
                        System.out.println("nouveau taux : " + nbBytesPerSecond);
                        startDownload = 0;
                        nbBytesInSecond = 0;
                    }
                }
                System.out.println("Download de la partie " + indexPacketToDownload + " finie...\nTotal DL : " + totalNbByteRead + "/" + message.getFileSize());
                pstmt.setLong(1, totalNbByteRead);
                pstmt.execute();
                sqlConn.commit();
                buffer.flip();
                fileChannel.write(buffer);
                setStateOfPart(indexPacketToDownload, "D");
            }
            askIndexPart(-1);
            socketChannel.close();
            fileChannel.close();
            String RSQL;
            if (getNbPartsToDownload() == 0) {
                File newFile = new File(Config.getInstance().getProperty("income_directory") + "/" + message.getFilename());
                if (newFile.exists()) newFile.delete();
                downloadingFile.renameTo(newFile);
                RSQL = "DELETE FROM downloading WHERE id_download=" + idDownload;
            } else RSQL = "UPDATE downloading SET state='P' WHERE id_download=" + idDownload;
            System.out.println(RSQL);
            sqlConn.createStatement().execute(RSQL);
            sqlConn.commit();
            System.out.println("FIN du transfert!!");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }
