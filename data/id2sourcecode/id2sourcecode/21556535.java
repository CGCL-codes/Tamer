    public static String debugSocket(final Socket socket) throws SocketException {
        StringBuilder str = new StringBuilder();
        str.append("Socket is =" + socket);
        str.append("is socket bound? =" + socket.isBound());
        str.append("is socket Connected? =" + socket.isConnected());
        str.append("show time out = " + socket.getSoTimeout());
        str.append("so linger =" + socket.getSoLinger());
        str.append("TCP no Delay =" + socket.getTcpNoDelay());
        str.append("Traffic class =" + socket.getTrafficClass());
        str.append("channel is =" + socket.getChannel());
        str.append("Reuse address is =" + socket.getReuseAddress());
        str.append("close =" + socket.isClosed());
        InetAddress in = socket.getInetAddress();
        str.append(in);
        str.append("\n");
        System.out.print("RAW IP Address - (byte[]) : ");
        byte[] b1 = in.getAddress();
        for (int i = 0; i < b1.length; i++) {
            if (i > 0) {
                System.out.print(".");
            }
            System.out.print(b1[i]);
        }
        str.append("Is Loopback Address?      : " + in.isLoopbackAddress());
        str.append("Is Multicast Address?     : " + in.isMulticastAddress());
        str.append("\n");
        InetAddress address = socket.getLocalAddress();
        str.append("Local address =" + address);
        return str.toString();
    }
