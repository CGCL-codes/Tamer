    public void list(PrintStream out) {
        out.println("-- ClientSocket --");
        try {
            out.println("Keep Alive           : " + getKeepAlive());
            out.println("Receive Buffer Size  : " + getReceiveBufferSize());
            out.println("Send Buffer Size     : " + getSendBufferSize());
            out.println("Is Socket Bound?     : " + isBound());
            out.println("Is Socket Connected? : " + isConnected());
            out.println("Is Socket Closed?    : " + isClosed());
            out.println("So Timeout           : " + getSoTimeout());
            out.println("So Linger            : " + getSoLinger());
            out.println("TCP No Delay         : " + getTcpNoDelay());
            out.println("Traffic Class        : " + getTrafficClass());
            out.println("Socket Channel       : " + getChannel());
            out.println("Reuse Address?       : " + getReuseAddress());
            out.println("\n");
            InetAddress inetAddrServer = getInetAddress();
            out.println("---------------------------");
            out.println("Remote (Server) Information");
            out.println("---------------------------");
            out.println("InetAddress - (Structure) : " + inetAddrServer);
            out.println("Socket Address - (Remote) : " + getRemoteSocketAddress());
            out.println("Canonical Name            : " + inetAddrServer.getCanonicalHostName());
            out.println("Host Name                 : " + inetAddrServer.getHostName());
            out.println("Host Address              : " + inetAddrServer.getHostAddress());
            out.println("Port                      : " + getPort());
            out.print("RAW IP Address - (byte[]) : ");
            byte[] b1 = inetAddrServer.getAddress();
            for (int i = 0; i < b1.length; i++) {
                if (i > 0) {
                    out.print(".");
                }
                out.print(b1[i] & 0xff);
            }
            out.println("\n");
            out.println("Is Loopback Address?      : " + inetAddrServer.isLoopbackAddress());
            out.println("Is Multicast Address?     : " + inetAddrServer.isMulticastAddress());
            out.println("\n");
            InetAddress inetAddrClient = getLocalAddress();
            out.println("--------------------------");
            out.println("Local (Client) Information");
            out.println("--------------------------");
            out.println("InetAddress - (Structure) : " + inetAddrClient);
            out.println("Socket Address - (Local)  : " + getLocalSocketAddress());
            out.println("Canonical Name            : " + inetAddrClient.getCanonicalHostName());
            out.println("Host Name                 : " + inetAddrClient.getHostName());
            out.println("Host Address              : " + inetAddrClient.getHostAddress());
            out.println("Port                      : " + getLocalPort());
            out.println("RAW IP Address - (byte[]) : ");
            byte[] b2 = inetAddrClient.getAddress();
            for (int i = 0; i < b2.length; i++) {
                if (i > 0) {
                    out.print(".");
                }
                out.print(b2[i] & 0xff);
            }
            out.println("\n");
            out.println("Is Loopback Address?      : " + inetAddrClient.isLoopbackAddress());
            out.println("Is Multicast Address?     : " + inetAddrClient.isMulticastAddress());
            out.println();
        } catch (IOException e) {
            e.printStackTrace();
        }
        out.flush();
    }
