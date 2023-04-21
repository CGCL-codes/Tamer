    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java jfipaClient.java <hostname> <portnumber> < file");
            System.exit(0);
        }
        String hostname = args[0];
        int portnumber = Integer.parseInt(args[1]);
        System.out.println("==> jfipaClient");
        System.out.println(" hostname   = " + hostname);
        System.out.println(" portnumber = " + portnumber);
        Socket jfipaPeer = null;
        try {
            jfipaPeer = new Socket(hostname, portnumber);
        } catch (Exception e) {
            System.err.println("could't open connection");
            System.exit(1);
        }
        System.out.println("opened socket to server peer");
        DataOutputStream dos = null;
        DataInputStream dis = null;
        try {
            dos = new DataOutputStream(jfipaPeer.getOutputStream());
        } catch (Exception e) {
            System.err.println("coudldn't open data outputstream");
            System.exit(1);
        }
        System.out.println("got stream handle from peer");
        try {
            dis = new DataInputStream(System.in);
        } catch (Exception e) {
            System.err.println("couldn't open data input stream");
            System.exit(1);
        }
        System.out.println("opened standard in");
        int pos = 0;
        byte readByte = (byte) 0;
        try {
            while (true) {
                dos.writeByte(dis.readByte());
                pos++;
            }
        } catch (Exception e) {
            System.out.println("eof");
        }
        System.out.println("wrote " + pos + " bytes to jfipaServer");
        boolean handshake = false;
        try {
            handshake = dis.readBoolean();
        } catch (Exception e) {
            System.out.println("couldn't read handshake");
        }
        System.out.println("handshake = " + handshake);
        System.out.println("Close connection");
        try {
            jfipaPeer.close();
        } catch (Exception e) {
            System.err.println("jfipaPeer.close() failed");
            System.exit(1);
        }
        System.out.println("<== jfipaClient");
    }
