    private static SocketFacade connectSock5(DestAddress address, Runnable acquireCallback) throws IOException {
        SocketFacade socket = null;
        InputStream is = null;
        OutputStream os = null;
        try {
            socket = createSocket(new DefaultDestAddress(ProxyPrefs.Socks5Host.get(), ProxyPrefs.Socks5Port.get().intValue()), NetworkPrefs.TcpConnectTimeout.get().intValue(), acquireCallback);
            is = Channels.newInputStream(socket.getChannel());
            os = Channels.newOutputStream(socket.getChannel());
            byte[] header;
            if (ProxyPrefs.Socks5Authentication.get().booleanValue() && ProxyPrefs.Socks5User.get().length() > 0) {
                header = new byte[4];
                header[0] = (byte) 0x05;
                header[1] = (byte) 0x02;
                header[2] = (byte) 0x00;
                header[3] = (byte) 0x02;
            } else {
                header = new byte[3];
                header[0] = (byte) 0x05;
                header[1] = (byte) 0x01;
                header[2] = (byte) 0x00;
            }
            os.write(header, 0, header.length);
            int servVersion = is.read();
            if (servVersion != 0x05) {
                throw new IOException("Invalid SOCKS server version: " + servVersion);
            }
            byte servMethod = (byte) is.read();
            if (servMethod == (byte) 0xFF) {
                throw new IOException("SOCKS: No acceptable authentication.");
            }
            if (servMethod == 0x00) {
            } else if (servMethod == 0x02) {
                authenticateUserPassword(is, os);
            } else {
                throw new IOException("Unknown SOCKS5 authentication method required.");
            }
            String host = address.getHostName();
            int port = address.getPort();
            byte[] request = new byte[10];
            request[0] = (byte) 0x05;
            request[1] = (byte) 0x01;
            request[2] = (byte) 0x00;
            request[3] = (byte) 0x01;
            IOUtil.serializeIP(host, request, 4);
            request[8] = (byte) (port >> 8);
            request[9] = (byte) (port);
            os.write(request, 0, request.length);
            int version = is.read();
            int status = is.read();
            switch(status) {
                case 0x01:
                    throw new IOException("SOCKS: General SOCKS server failure");
                case 0x02:
                    throw new IOException("SOCKS: Connection not allowed by ruleset");
                case 0x03:
                    throw new IOException("SOCKS: Network unreachable");
                case 0x04:
                    throw new SocketException("SOCKS: Host unreachable");
                case 0x05:
                    throw new SocketException("SOCKS: Connection refused");
                case 0x06:
                    throw new IOException("SOCKS: TTL expired");
                case 0x07:
                    throw new IOException("SOCKS: Command not supported");
                case 0x08:
                    throw new IOException("SOCKS: Address type not supported");
            }
            if (status != 0x00) {
                throw new IOException("SOCKS: Unknown status response: " + status);
            }
            is.read();
            int atype = is.read();
            if (atype == 1) {
                is.read();
                is.read();
                is.read();
                is.read();
            } else if (atype == 3) {
                int len = is.read();
                if (len < 0) len += 256;
                while (len > 0) {
                    is.read();
                    len--;
                }
            } else if (atype == 4) {
                for (int i = 0; i < 16; i++) is.read();
            } else {
                throw new IOException("Invalid return address type for SOCKS5: " + atype);
            }
            is.read();
            is.read();
            if (version != 0x05) {
                throw new IOException("Invalid SOCKS server version: " + version);
            }
            return socket;
        } catch (Exception exp) {
            IOUtil.closeQuietly(is);
            IOUtil.closeQuietly(os);
            IOUtil.closeQuietly(socket);
            if (exp instanceof IOException) {
                throw (IOException) exp;
            } else {
                throw new IOException("Error: " + exp.getMessage());
            }
        }
    }
