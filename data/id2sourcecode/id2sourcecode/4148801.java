    public void run() {
        byte[] readbuf = new byte[__tun.MAX_PACKET_LENGTH];
        byte[] writebuf = new byte[__tun.MAX_PACKET_LENGTH];
        byte[] no_eth_frame;
        byte[] src4 = new byte[4];
        byte[] src6 = new byte[16];
        byte[] dst4 = new byte[4];
        byte[] dst6 = new byte[16];
        byte[] mac = new byte[6];
        byte[] __full_frame;
        ARPPacket __arp = new ARPPacket(1);
        int readNum = 0;
        int writeNum = 0;
        while (true) {
            try {
                if ((readNum = __tun.read(readbuf)) > 0) {
                    readbuf = Helpers.toUnsignedByteArray(readbuf);
                    no_eth_frame = new byte[readNum - JtunDevice.ETH_HDR_LENGTH];
                    System.arraycopy(readbuf, JtunDevice.ETH_HDR_LENGTH, no_eth_frame, 0, readNum - JtunDevice.ETH_HDR_LENGTH);
                    if (__handleARPPackets && JtunDevice.isARPRequest(readbuf)) {
                        System.out.println("ARP packet");
                        __full_frame = new byte[no_eth_frame.length + 14];
                        __arp.setData(no_eth_frame);
                        __arp.getSenderHdwAddr(mac);
                        __arp.setDestHdwAddr(mac);
                        __arp.setOpCode(ARPPacket.ARP_REPLY);
                        __arp.getDestProtoAddr(dst4);
                        __arp.getSourceProtoAddr(src4);
                        __arp.setDestProtoAddr(src4);
                        __arp.setSourceProtoAddr(dst4);
                        if (getAppendEther()) __tun.setEthHeader(__full_frame, no_eth_frame, mac, mac, (short) 0x0806);
                        try {
                            if ((writeNum = __tun.write(__full_frame)) < readNum) {
                                System.out.println("ARP reply not sent");
                            } else {
                                System.out.println("ARP reply sent " + writeNum + " bytes");
                            }
                        } catch (Exception ex) {
                        }
                    } else if (JtunDevice.isIPv4Packet(readbuf) || JtunDevice.isIPv6Packet(readbuf)) {
                        for (int i = 0; i < __listeners.size(); i++) {
                            IPPacket packet = new IPPacket(no_eth_frame.length);
                            packet.setData(no_eth_frame);
                            if (__listeners.get(i).matches(packet, this)) __listeners.get(i).receivePacket(packet, this);
                        }
                    }
                }
            } catch (IOException e) {
            }
        }
    }
