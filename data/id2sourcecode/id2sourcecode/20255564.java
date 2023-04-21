    private void readFiles(byte[] buffer) throws InterruptedException, IOException {
        int available = 0;
        boolean sentAck = false;
        FileOutputStream oStream = null;
        FileInputStream iStream = null;
        File tempFile = null;
        while (true) {
            Thread.sleep(100);
            available = clientConn.getInputStream().read(buffer);
            if (available == -1) {
                break;
            }
            if (available > 0) {
                if (oStream != null) {
                    byte[] writebuffer = null;
                    writebuffer = new byte[available];
                    for (int i = 0; i < writebuffer.length; i++) {
                        writebuffer[i] = buffer[i];
                    }
                    oStream.write(writebuffer);
                    oStream.flush();
                    if (available >= 9000) continue;
                }
            }
            if (buffer[0] == 'N' && buffer[1] == 'E' && buffer[2] == 'X' && buffer[3] == 'T' && buffer[4] == 'F' && buffer[5] == 'I' && buffer[6] == 'L' && buffer[7] == 'E' || readHeader(buffer, available)) {
                for (int i = 0; i < available; i++) {
                    System.out.print((char) buffer[i]);
                }
                System.out.println("");
                clientConn.getOutputStream().write(new byte[] { 'R', 'E', 'A', 'D', 'Y' }, 0, 5);
                clientConn.getOutputStream().flush();
                tempFile = File.createTempFile("temp", "");
                oStream = new FileOutputStream(tempFile);
                iStream = new FileInputStream(tempFile);
                sentAck = false;
            } else if (buffer[available - 1] == -1 && !sentAck) {
                oStream.getChannel().position(oStream.getChannel().position() - 5);
                clientConn.getOutputStream().write(new byte[] { 'A', 'C', 'K' }, 0, 3);
                clientConn.getOutputStream().flush();
                File file = File.createTempFile("Unreal.ngLog." + clientIpAddress + ".", ".log", logDir);
                FileOutputStream uncomStream = ZlibDecompressUtil.uncompressLogFile(iStream, file);
                oStream.close();
                iStream.close();
                uncomStream.close();
                tempFile.delete();
                oStream = null;
                iStream = null;
                sentAck = true;
            }
        }
    }
