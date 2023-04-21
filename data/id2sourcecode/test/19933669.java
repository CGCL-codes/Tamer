    public InetAddress[] getValueAsInetAddrs() throws IllegalArgumentException {
        if (!OptionFormat.INETS.equals(_DHO_FORMATS.get(code))) {
            throw new IllegalArgumentException("DHCP option type (" + code + ") is not InetAddr[]");
        }
        if (value == null) throw new IllegalStateException("value is null");
        if ((value.length % 4) != 0) throw new DHCPBadPacketException("option " + code + " is wrong size:" + value.length + " should be 4*X");
        try {
            byte[] addr = new byte[4];
            InetAddress[] addrs = new InetAddress[value.length / 4];
            for (int i = 0, a = 0; a < value.length; i++, a += 4) {
                addr[0] = value[a];
                addr[1] = value[a + 1];
                addr[2] = value[a + 2];
                addr[3] = value[a + 3];
                addrs[i] = InetAddress.getByAddress(addr);
            }
            return addrs;
        } catch (UnknownHostException e) {
            logger.log(Level.SEVERE, "Unexpected UnknownHostException", e);
            return null;
        }
    }
