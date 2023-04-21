    private static void displayInterfaceInformation(NetworkInterface netint) throws SocketException {
        console.printf("Display name: %s%n", netint.getDisplayName());
        console.printf("Name: %s%n", netint.getName());
        Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
        for (InetAddress inetAddress : Collections.list(inetAddresses)) {
            console.printf("InetAddress: %s%n", inetAddress);
        }
        console.printf("Parent: %s%n", netint.getParent());
        console.printf("Up? %s%n", netint.isUp());
        console.printf("Loopback? %s%n", netint.isLoopback());
        console.printf("PointToPoint? %s%n", netint.isPointToPoint());
        console.printf("Supports multicast? %s%n", netint.isVirtual());
        console.printf("Virtual? %s%n", netint.isVirtual());
        console.printf("Hardware address: %s%n", Arrays.toString(netint.getHardwareAddress()));
        console.printf("MTU: %s%n", netint.getMTU());
        List<InterfaceAddress> interfaceAddresses = netint.getInterfaceAddresses();
        for (InterfaceAddress addr : interfaceAddresses) {
            console.printf("InterfaceAddress: %s%n", addr.getAddress());
        }
        console.printf("%n");
        Enumeration<NetworkInterface> subInterfaces = netint.getSubInterfaces();
        for (NetworkInterface networkInterface : Collections.list(subInterfaces)) {
            console.printf("%nSubInterface%n");
            displayInterfaceInformation(networkInterface);
        }
        console.printf("%n");
    }
