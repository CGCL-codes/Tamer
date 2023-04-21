    void logoff(boolean inError) {
        synchronized (transport) {
            try {
                if (sessionSetup == false) {
                    return;
                }
                for (Enumeration e = trees.elements(); e.hasMoreElements(); ) {
                    SmbTree t = (SmbTree) e.nextElement();
                    t.treeDisconnect(inError);
                }
                if (transport.server.security == ServerMessageBlock.SECURITY_SHARE) {
                    return;
                }
                if (!inError) {
                    SmbComLogoffAndX request = new SmbComLogoffAndX(null);
                    request.uid = uid;
                    try {
                        transport.send(request, null);
                    } catch (SmbException se) {
                    }
                }
                sessionSetup = false;
            } finally {
                transport = SmbTransport.NULL_TRANSPORT;
            }
        }
    }
