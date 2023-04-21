    public void connect() throws IOException, VNCException.AuthenticationFailed {
        if (isConnected()) {
            throw new IllegalStateException();
        }
        System.out.println("vnc: trying to open " + host + ":" + port);
        socket = new Socket(host, port);
        in = new BufferedInputStream(socket.getInputStream());
        String versionOffer = new String(read(12));
        System.out.println("version offered: " + versionOffer.trim());
        int versionMajor = Integer.parseInt(versionOffer.substring(6, 7));
        int versionMinor = Integer.parseInt(versionOffer.substring(10, 11));
        System.out.println("version: " + versionMajor + "." + versionMinor);
        String versionRequest = "RFB 003.008\n";
        System.out.println("requesting version: " + versionRequest.trim());
        write(versionRequest.getBytes());
        byte numSecTypes = read(1)[0];
        byte[] secTypes = read(numSecTypes);
        byte secType = -1;
        for (int i = 0; i < secTypes.length; i++) {
            byte type = secTypes[i];
            System.out.println("offered sec type: " + type);
            if (type == 1 || type == 2) {
                secType = type;
                break;
            }
        }
        switch(secType) {
            case SECURITY_VNC:
                write(2, 1);
                byte[] challenge = read(16);
                System.out.println("challenge: " + dump(challenge));
                byte[] secretBytes = secret.getBytes();
                swapBits(secretBytes);
                byte[] response;
                try {
                    SecretKey key = SecretKeyFactory.getInstance("DES").generateSecret(new DESKeySpec(secretBytes));
                    Cipher ecipher = Cipher.getInstance("DES/ECB/NoPadding");
                    ecipher.init(Cipher.ENCRYPT_MODE, key);
                    response = ecipher.doFinal(challenge);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
                System.out.println("sending response: " + write(response));
                break;
            default:
                throw new IllegalArgumentException("security types not supported");
        }
        int secResult = readInt(4);
        System.out.println("security response: " + secResult);
        connected = secResult == 0;
        if (!connected) {
            socket.close();
            socket = null;
            throw new VNCException.AuthenticationFailed();
        }
        System.out.println("sending client init: " + write(1, 1));
        framebufferWidth = readInt(2);
        framebufferHeight = readInt(2);
        System.out.println("framebuffer: " + framebufferWidth + "x" + framebufferHeight);
        pixelFormat = new PixelFormat(read(16));
        if (pixelFormat.getDepth() != 24) {
            throw new IllegalArgumentException("only depth 24 supported");
        }
        name = new String(read(readLong(4)), "ASCII");
        System.out.println("name: " + name);
        System.out.println("sending encodings request: " + write(new byte[] { 2, 0, 0, 4 }) + write(ENCODING_TIGHT, 4) + write(ENCODING_TIGHT_OPTION_QUALITY + 5, 4) + write(ENCODING_RICHCURSOR, 4) + write(ENCODING_POINTERPOS, 4));
    }
