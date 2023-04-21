    static byte[] crypt(byte versionNumber, byte sequenceNumber, byte[] body, byte headerFlags, byte[] sessionID, byte[] secretkey) throws IOException, NoSuchAlgorithmException {
        if (headerFlags == Tacacs.HEADERFLAG_UNENCRYPT) return body;
        MessageDigest md = null;
        md = MessageDigest.getInstance("MD5");
        byte[] pad = null;
        byte[] lastPad = null;
        boolean keepLoop = true;
        while (keepLoop) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            baos.write(sessionID);
            baos.write(secretkey);
            baos.write(versionNumber);
            baos.write(sequenceNumber);
            if (lastPad != null) baos.write(lastPad);
            lastPad = md.digest(baos.toByteArray());
            baos.reset();
            if (pad != null) baos.write(pad);
            baos.write(lastPad);
            pad = baos.toByteArray();
            if (pad.length > body.length) keepLoop = false;
        }
        byte[] realBody = new byte[body.length];
        for (int i = 0; i < body.length; i++) {
            realBody[i] = Bytes.InttoByte(Bytes.BytetoInt(body[i]) ^ Bytes.BytetoInt(pad[i]));
        }
        return realBody;
    }
