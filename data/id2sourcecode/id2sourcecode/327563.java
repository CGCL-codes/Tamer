    public static void main(String[] args) throws Exception {
        if (args.length != 3) {
            System.err.println("TestSocket host port localport");
            return;
        }
        String remoteHost = args[0];
        int remotePort = Integer.parseInt(args[1]), localPort = Integer.parseInt(args[2]);
        OSIDataLinkDevice device = OSIDataLinkDevice.getDevices()[0];
        InetAddress localAddr = null;
        {
            TCPSocket tcpsock = new TCPSocket(device, null, null);
            Socket.setSocketImplFactory(tcpsock);
            ServerSocket.setSocketFactory(tcpsock);
        }
        try {
            device.startCapture();
            Socket sock;
            try {
                sock = new Socket(remoteHost, remotePort, localAddr, localPort);
            } catch (IOException ioe) {
                ioe.printStackTrace();
                System.err.println("Connection failed.  Trying listen instead.");
                ServerSocket ss = new ServerSocket(localPort, 0, localAddr);
                sock = ss.accept();
            }
            System.err.println("Connected!  Enter lines to send.  Hit ^Z to end.");
            OutputStream sockOS = sock.getOutputStream();
            final InputStream sockIS = sock.getInputStream();
            new Thread(new Runnable() {

                public void run() {
                    try {
                        int ch;
                        while ((ch = sockIS.read()) != -1) System.out.write(ch);
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                    System.err.println("End of Stream");
                }
            }).start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String line;
            while ((line = reader.readLine()) != null) sockOS.write((line + '\n').getBytes());
            sock.close();
        } finally {
            device.stopCapture();
        }
    }
