    public void connect(String IP) throws EIBConnectionNotPossibleException {
        if (!isConnected()) {
            serverCtrlIP = IP;
            serverDataIP = "";
            serverDataPort = 0;
            channelID = 0;
            serverSeqCounter = 0;
            localSeqCounter = 0;
            if (socket != null) try {
                socket.close();
            } catch (Exception e) {
            }
            try {
                socket = new DatagramSocket(localPort);
            } catch (Exception e) {
            }
            if (socket == null) {
                throw new EIBConnectionNotPossibleException("Port is busy");
            }
            try {
                KNXnetIPConRequest request = new KNXnetIPConRequest(InetAddress.getLocalHost().getHostAddress(), localPort);
                request.send(socket, serverCtrlIP, serverCtrlPort);
            } catch (Exception e) {
                logger.debug("Datagram error: " + e);
                return;
            }
            KNXnetIPConResponse response = new KNXnetIPConResponse();
            receive(response);
            if (response.getStatus() != KNXnetIPConResponse.E_NO_ERROR) throw new EIBConnectionNotPossibleException("KNXnet/IP error: server refused connection");
            frames.clear();
            channelID = response.getChannelID();
            serverDataIP = response.getDataEndPoint().getIP();
            serverDataPort = response.getDataEndPoint().getPort();
            int ia = response.getDesc().getIndividualAddress();
            individualAddress = new EIBPhaddress((ia >>> 12) & 0x0F, (ia >>> 8) & 0x0F, ia & 0xFF);
            setConnected(true);
            logger.debug("KNXnet/IP connection opened");
            conState = new HeartbeatMonitor();
            conState.start();
            receiver = new KNXnetIPReceiver();
            receiver.start();
        }
    }
