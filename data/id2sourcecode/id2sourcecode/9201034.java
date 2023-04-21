    protected void createPacket() {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] xmlData = xml.getXMLByteData();
            ByteArrayOutputStream baos;
            long size;
            byte[] md5Sum = null;
            if (tcpPacket == false) {
                digest.update(xmlData);
                md5Sum = digest.digest();
                size = MESSEGE_TYPE_SIZE + MD5SUM_SIZE + xmlData.length;
                baos = new ByteArrayOutputStream(SIZE_SIZE + MESSEGE_TYPE_SIZE + MD5SUM_SIZE + xmlData.length);
            } else {
                size = MESSEGE_TYPE_SIZE + xmlData.length;
                baos = new ByteArrayOutputStream(SIZE_SIZE + MESSEGE_TYPE_SIZE + xmlData.length);
            }
            DataOutputStream dos = new DataOutputStream(baos);
            dos.writeLong(size);
            dos.writeByte(type);
            if (tcpPacket == false) dos.write(md5Sum);
            dos.write(xmlData);
            packetData = baos.toByteArray();
            if (tcpPacket == false) packet = new DatagramPacket(packetData, packetData.length, NetworkConfiguration.getInstance().getMulticastAddress(), NetworkConfiguration.getInstance().getPort());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
