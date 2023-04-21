    public TelnetClient(InputStream in, PrintStream out, String host, int port) {
        TelnetWrapper telnetWrapper = new TelnetWrapper();
        try {
            telnetWrapper.connect(host, port);
        } catch (IOException e) {
            System.out.println("TelnetClient: Got exception during connect: " + e);
            e.printStackTrace();
        }
        createAndStartReader(telnetWrapper, out);
        byte[] buf = new byte[256];
        int n = 0;
        try {
            while (n >= 0) {
                n = in.read(buf);
                if (n > 0) {
                    byte[] sendBuf = new byte[n];
                    System.arraycopy(buf, 0, sendBuf, 0, n);
                    telnetWrapper.getHandler().transpose(sendBuf);
                }
            }
        } catch (IOException e) {
            System.out.println("TelnetClient: Got exception in read/write loop: " + e);
            e.printStackTrace();
            return;
        } finally {
            try {
                telnetWrapper.disconnect();
            } catch (IOException e) {
                System.out.println("TelnetClient: got exception in disconnect: " + e);
                e.printStackTrace();
            }
        }
    }
