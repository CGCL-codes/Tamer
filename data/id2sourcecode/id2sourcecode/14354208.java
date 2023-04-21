    public String get(InetAddress address, int port, String oid) throws SocketException {
        System.out.println("get(" + address + ", " + port + ", " + oid + ")");
        System.out.println("get: " + readCommunity + ", " + writeCommunity + ", " + timeout + ", " + retries);
        SnmpPeer peer = new SnmpPeer(address, port);
        peer.setTimeout(timeout);
        peer.setRetries(retries);
        SnmpParameters params = peer.getParameters();
        params.setVersion(SnmpSMI.SNMPV2);
        params.setReadCommunity(readCommunity);
        params.setWriteCommunity(writeCommunity);
        peer.setParameters(params);
        final Vector snmpVarBinds = new Vector();
        final SnmpSession session = new SnmpSession(peer);
        session.setDefaultHandler(new SnmpHandler() {

            public void snmpTimeoutError(SnmpSession session, SnmpSyntax pdu) {
                System.out.println(session.getPeer().getPeer());
                System.out.println(pdu.getClass().getName());
                System.out.println("SnmpTimeout");
                synchronized (session) {
                    session.notify();
                }
            }

            public void snmpInternalError(SnmpSession session, int err, SnmpSyntax pdu) {
                System.out.println("InternalError");
                synchronized (session) {
                    session.notify();
                }
            }

            public void snmpReceivedPdu(SnmpSession session, int command, SnmpPduPacket pdu) {
                for (int i = 0; i < pdu.getLength(); i++) {
                    SnmpVarBind varBind = pdu.getVarBindAt(i);
                    System.out.println("Received value: " + varBind.getName() + "=" + varBind.getValue());
                    snmpVarBinds.addElement(varBind);
                }
                synchronized (session) {
                    session.notify();
                }
            }
        });
        SnmpVarBind[] vblist = { new SnmpVarBind(oid) };
        SnmpPduRequest pdu = new SnmpPduRequest(SnmpPduPacket.GET, vblist);
        pdu.setRequestId(1);
        try {
            synchronized (session) {
                session.send(pdu);
                session.wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String result = null;
        if (snmpVarBinds.size() > 0) {
            SnmpVarBind varBind = (SnmpVarBind) snmpVarBinds.get(0);
            SnmpSyntax value = varBind.getValue();
            if (value instanceof SnmpOctetString) {
                SnmpOctetString string = (SnmpOctetString) value;
                result = string.toString();
            }
        }
        try {
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
