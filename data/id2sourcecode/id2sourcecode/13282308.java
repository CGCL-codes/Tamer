        @Override
        public void sessionEstablished(Session session) {
            Socket socket = ((SocketSession) session).getChannel().socket();
            connection.setInternalIP(socket.getLocalAddress().getHostAddress());
            connection.setInternalPort(socket.getLocalPort());
            OutgoingVER ver = new OutgoingVER(null);
            ver.setSupportedProtocol(getSupportedProtocol());
            send(ver, false);
            OutgoingCVR cvr = new OutgoingCVR(null);
            cvr.setEmail(owner.getEmail());
            send(cvr, false);
            OutgoingUSRInitNS usr = new OutgoingUSRInitNS(null);
            usr.setEmail(owner.getEmail());
            send(usr, false);
        }
