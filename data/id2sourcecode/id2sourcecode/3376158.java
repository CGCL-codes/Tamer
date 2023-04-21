    public String toString() {
        String str = "";
        str = getClass().getName() + " [\n";
        try {
            str += "-------------------------" + "\n";
            str += "   Socket Information    " + "\n";
            str += "-------------------------" + "\n";
            str += "socket               : " + this + "\n";
            str += "Keep Alive           : " + getKeepAlive() + "\n";
            str += "Receive Buffer Size  : " + getReceiveBufferSize() + "\n";
            str += "Send Buffer Size     : " + getSendBufferSize() + "\n";
            str += "Is Socket Bound?     : " + isBound() + "\n";
            str += "Is Socket Connected? : " + isConnected() + "\n";
            str += "Is Socket Closed?    : " + isClosed() + "\n";
            str += "So Timeout           : " + getSoTimeout() + "\n";
            str += "So Linger            : " + getSoLinger() + "\n";
            str += "TCP No Delay         : " + getTcpNoDelay() + "\n";
            str += "Traffic Class        : " + getTrafficClass() + "\n";
            str += "Socket Channel       : " + getChannel() + "\n";
            str += "Reuse Address?       : " + getReuseAddress() + "\n";
            str += "\n\n";
            InetAddress inetAddrServer = getInetAddress();
            str += "---------------------------" + "\n";
            str += "Remote (Server) Information" + "\n";
            str += "---------------------------" + "\n";
            str += "InetAddress - (Structure) : " + inetAddrServer + "\n";
            str += "Socket Address - (Remote) : " + getRemoteSocketAddress() + "\n";
            str += "Canonical Name            : " + inetAddrServer.getCanonicalHostName() + "\n";
            str += "Host Name                 : " + inetAddrServer.getHostName() + "\n";
            str += "Host Address              : " + inetAddrServer.getHostAddress() + "\n";
            str += "Port                      : " + getPort();
            str += "RAW IP Address - (byte[]) : ";
            byte[] b1 = inetAddrServer.getAddress();
            for (int i = 0; i < b1.length; i++) {
                if (i > 0) {
                    str += ".";
                }
                str += b1[i] & 0xff;
            }
            str += "\n";
            str += "Is Loopback Address?      : " + inetAddrServer.isLoopbackAddress() + "\n";
            str += "Is Multicast Address?     : " + inetAddrServer.isMulticastAddress() + "\n";
            str += "\n\n";
            InetAddress inetAddrClient = getLocalAddress();
            str += "--------------------------" + "\n";
            str += "Local (Client) Information" + "\n";
            str += "--------------------------" + "\n";
            str += "InetAddress - (Structure) : " + inetAddrClient + "\n";
            str += "Socket Address - (Local)  : " + getLocalSocketAddress() + "\n";
            str += "Canonical Name            : " + inetAddrClient.getCanonicalHostName() + "\n";
            str += "Host Name                 : " + inetAddrClient.getHostName() + "\n";
            str += "Host Address              : " + inetAddrClient.getHostAddress() + "\n";
            str += "Port                      : " + getLocalPort() + "\n";
            str += "RAW IP Address - (byte[]) : ";
            byte[] b2 = inetAddrClient.getAddress();
            for (int i = 0; i < b2.length; i++) {
                if (i > 0) {
                    str += ".";
                }
                str += b2[i] & 0xff;
            }
            str += "\n";
            str += "Is Loopback Address?      : " + inetAddrClient.isLoopbackAddress() + "\n";
            str += "Is Multicast Address?     : " + inetAddrClient.isMulticastAddress() + "\n";
            str += "\n";
            str += "]";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }
