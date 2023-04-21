    public static InetSocketAddress getNewInetSocketAddressWithRandomPort(InetAddress addr) {
        int portNum = 1024 + random.nextInt(65535 - 1024);
        InetSocketAddress localAddr = new InetSocketAddress(addr, portNum);
        return localAddr;
    }
