    private void showServerSocket(ServerSocket socket) {
        try {
            System.out.println("-------------------------");
            System.out.println("Server Socket Information");
            System.out.println("-------------------------");
            System.out.println("serverSocket         : " + socket);
            System.out.println("Receive Buffer Size  : " + socket.getReceiveBufferSize());
            System.out.println("Is Socket Bound?     : " + socket.isBound());
            System.out.println("Is Socket Closed?    : " + socket.isClosed());
            System.out.println("So Timeout           : " + socket.getSoTimeout());
            System.out.println("Socket Channel       : " + socket.getChannel());
            System.out.println("Reuse Address?       : " + socket.getReuseAddress());
            System.out.println("\n");
            InetAddress inetAddrServer = socket.getInetAddress();
            System.out.println("---------------------------");
            System.out.println("Remote (Server) Information");
            System.out.println("---------------------------");
            System.out.println("InetAddress - (Structure): " + inetAddrServer);
            System.out.println("Canonical Name           : " + inetAddrServer.getCanonicalHostName());
            System.out.println("Host Name                : " + inetAddrServer.getHostName());
            System.out.println("Host Address             : " + inetAddrServer.getHostAddress());
            System.out.print("RAW IP Address - (byte[]): ");
            byte[] b1 = inetAddrServer.getAddress();
            for (int i = 0; i < b1.length; i++) {
                if (i > 0) {
                    System.out.print(".");
                }
                System.out.print(b1[i] & 0xff);
            }
            System.out.println();
            System.out.println("Is Loopback Address?  : " + inetAddrServer.isLoopbackAddress());
            System.out.println("Is Multicast Address? : " + inetAddrServer.isMulticastAddress());
            System.out.println("\n");
            SocketAddress inetAddrClient = socket.getLocalSocketAddress();
            System.out.println("--------------------------");
            System.out.println("Local (Client) Information");
            System.out.println("--------------------------");
            System.out.println("InetAddress - (Structure): " + inetAddrClient);
            System.out.println("Socket Address - (Local) : " + socket.getLocalSocketAddress());
            System.out.println("Port                     : " + socket.getLocalPort());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
