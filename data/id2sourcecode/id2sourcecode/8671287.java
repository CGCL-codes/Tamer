    public static void main(String[] args) throws IOException {
        InetAddress ia = InetAddress.getByName("225.0.0.1");
        InetAddress iF = InetAddress.getLocalHost();
        MulticastSocket ms = new MulticastSocket(59218);
        ms.joinGroup(ia);
        ms.setInterface(iF);
        DatagramPacket p = new DatagramPacket(new byte[10], 10);
        ms.receive(p);
        p.getAddress();
        ms.setTimeToLive(1);
        ms.getChannel().configureBlocking(true);
    }
