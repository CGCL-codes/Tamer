    public boolean sendFile(String filePath, String targetIP, int targetPort) {
        File file = new File(filePath);
        try {
            info("[FTS] Prepare to transport file: " + file.getCanonicalPath());
            if (!file.isFile()) {
                error("[FTS] Illegal file: " + file.getCanonicalPath());
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Socket client = super.connectToTCP(targetIP, targetPort);
        if (null == client) {
            warn("[FTS] Connect to: " + targetIP + ":" + targetPort + " failed!");
            return false;
        }
        try {
            DataInputStream fis = new DataInputStream(new BufferedInputStream(new FileInputStream(filePath)));
            DataOutputStream dos = new DataOutputStream(client.getOutputStream());
            byte[] buffer = new byte[this.bufferSize];
            String fileName = file.getName();
            dos.writeShort(fileName.length());
            dos.writeChars(fileName);
            dos.writeLong(file.length());
            while (true) {
                int readByte = 0;
                readByte = fis.read(buffer);
                if (readByte == -1) {
                    break;
                }
                dos.write(buffer, 0, readByte);
            }
            dos.flush();
            dos.close();
            fis.close();
            client.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
