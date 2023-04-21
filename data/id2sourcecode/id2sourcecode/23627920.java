    public void encode(DatagramPacket packet) throws Exception {
        Host h = new Host(packet.getAddress(), packet.getPort());
        HostKey hostKey = (HostKey) hostKeys.get(h);
        if (hostKey == null) {
            hostKey = new HostKey();
            hostKey.hasOwnPK = true;
            hostKeys.put(h, hostKey);
            byte[] data = packet.getData();
            int o = packet.getOffset();
            int l = packet.getLength();
            int rest = data.length - packet.getOffset() - packet.getLength();
            if (rest >= encryptedPublicKey.length) {
                System.arraycopy(encryptedPublicKey, 0, data, o + l, encryptedPublicKey.length);
                packet.setData(data, o, l + encryptedPublicKey.length);
            } else {
                BufferOutputStream out = new BufferOutputStream(packet.getLength() + encryptedPublicKey.length);
                out.write(data, o, l);
                out.write(encryptedPublicKey);
                packet.setData(out.getBuffer(), 0, out.getPosition());
            }
        } else {
            SecretKey secretKey = hostKey.key;
            Cipher cipher = createCipher();
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encrypted = cipher.doFinal(packet.getData(), packet.getOffset(), packet.getLength());
            synchronized (hostKey) {
                if (hostKey.hasOwnPK) {
                    packet.setData(encrypted);
                } else {
                    byte[] buf = new byte[encrypted.length + encryptedPublicKey.length];
                    System.arraycopy(encrypted, 0, buf, 0, encrypted.length);
                    System.arraycopy(encryptedPublicKey, 0, buf, encrypted.length, encryptedPublicKey.length);
                    packet.setData(buf, 0, buf.length);
                    hostKey.hasOwnPK = true;
                }
            }
        }
    }
