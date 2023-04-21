    public TransportFilter createTransportFilter(Connection connection, TransportCipher read_cipher, TransportCipher write_cipher) throws TransportException {
        Transport transport = connection.getTransport();
        com.aelitis.azureus.core.networkmanager.Transport core_transport;
        try {
            core_transport = ((TransportImpl) transport).coreTransport();
        } catch (IOException e) {
            throw new TransportException(e);
        }
        TransportHelper helper;
        if (core_transport instanceof TCPTransportImpl) {
            TransportHelperFilter hfilter = ((TCPTransportImpl) core_transport).getFilter();
            if (hfilter != null) {
                helper = hfilter.getHelper();
            } else {
                helper = new TCPTransportHelper(((TCPTransportImpl) (core_transport)).getSocketChannel());
            }
        } else if (core_transport instanceof UDPTransport) {
            TransportHelperFilter hfilter = ((UDPTransport) core_transport).getFilter();
            if (hfilter != null) {
                helper = hfilter.getHelper();
            } else {
                helper = ((UDPTransport) core_transport).getFilter().getHelper();
                InetSocketAddress addr = core_transport.getTransportEndpoint().getProtocolEndpoint().getConnectionEndpoint().getNotionalAddress();
                if (!connection.isIncoming()) {
                    try {
                        helper = new UDPTransportHelper(UDPNetworkManager.getSingleton().getConnectionManager(), addr, (UDPTransport) core_transport);
                    } catch (IOException ioe) {
                        throw new TransportException(ioe);
                    }
                } else {
                    throw new TransportException("udp incoming transport type not supported - " + core_transport);
                }
            }
        } else {
            throw new TransportException("transport type not supported - " + core_transport);
        }
        TransportHelperFilterStreamCipher core_filter = new TransportHelperFilterStreamCipher(helper, ((TransportCipherImpl) read_cipher).cipher, ((TransportCipherImpl) write_cipher).cipher);
        return new TransportFilterImpl(core_filter);
    }
