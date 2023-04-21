    public static void main(String[] args) throws Exception {
        if (args.length != 3) {
            System.err.println("TestSocket host port localport");
            return;
        }
        OSIDataLinkDevice device = OSIDataLinkDevice.getDevices()[0];
        TCPSocket sock = new TCPSocket(device, null, null);
        device.startCapture();
        try {
            sock.bind(((EthernetDevice) device).findLocalMatch(), Integer.parseInt(args[2]));
            try {
                sock.connect(java.net.InetAddress.getByName(args[0]), Integer.parseInt(args[1]));
            } catch (IOException ioe) {
                ioe.printStackTrace();
                System.err.println("Connection failed.  Trying listen instead.");
                sock.listen(0);
                TCPSocket tempSock = new TCPSocket(device, sock.getTCPHandler(), null);
                sock.accept(tempSock);
                sock = tempSock;
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
