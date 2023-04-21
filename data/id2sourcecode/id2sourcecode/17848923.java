    void handle(Socket socket) throws IOException {
        sessionReader.open(sessionFile);
        sessionReader.readHeader();
        String infoLines[] = sessionReader.infoLines();
        for (String line : infoLines) {
            uo.info(line);
        }
        DataInputStream dis = new DataInputStream(socket.getInputStream());
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        dos.write(sessionReader.getRFBVersionMessage().getBytes());
        dis.readFully(readBuffer, 0, 12);
        dos.writeInt(1);
        int securityType = dis.readByte();
        if (securityType != RfbProto.SecTypeNone) {
            throw new IOException("VNC client requested security type " + securityType + " but we only support " + RfbProto.SecTypeNone + ".");
        }
        dos.writeShort(sessionReader.getFrameBufferWidth());
        dos.writeShort(sessionReader.getFrameBufferHeight());
        dos.write(sessionReader.getFBSServerInitMessage());
        dos.writeInt(sessionReader.getDesktopName().length());
        dos.write(sessionReader.getDesktopName().getBytes());
        byte[] postHeaderBuffer = sessionReader.getPostHeaderBuffer();
        if (postHeaderBuffer != null && postHeaderBuffer.length > 0) {
            dos.write(postHeaderBuffer);
        }
        long lastTime = -1;
        long time;
        int len;
        byte[] readBuffer;
        int availableByteCount;
        try {
            while (true) {
                sessionReader.readElement();
                time = sessionReader.getTime();
                len = sessionReader.getLen();
                readBuffer = sessionReader.getReadBuffer();
                dos.write(readBuffer, 0, len);
                if (lastTime != -1) {
                    Thread.sleep(time - lastTime);
                }
                availableByteCount = dis.available();
                if (availableByteCount > 0) {
                    dis.readFully(readBuffer, 0, availableByteCount);
                }
                lastTime = time;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (EOFException e) {
        } finally {
            try {
                dis.close();
            } catch (Exception e) {
            }
        }
        uo.info("Playback complete");
    }
