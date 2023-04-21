    private void configureStaticMembership() throws ClusteringFault {
        channel.setMembershipService(new WkaMembershipService(primaryMembershipManager));
        StaticMember localMember = new StaticMember();
        primaryMembershipManager.setLocalMember(localMember);
        ReceiverBase receiver = (ReceiverBase) channel.getChannelReceiver();
        Parameter localHost = getParameter(TribesConstants.LOCAL_MEMBER_HOST);
        String host;
        if (localHost != null) {
            host = ((String) localHost.getValue()).trim();
        } else {
            try {
                try {
                    host = Utils.getIpAddress();
                } catch (SocketException e) {
                    String msg = "Could not get local IP address";
                    log.error(msg, e);
                    throw new ClusteringFault(msg, e);
                }
            } catch (Exception e) {
                String msg = "Could not get the localhost name";
                log.error(msg, e);
                throw new ClusteringFault(msg, e);
            }
        }
        receiver.setAddress(host);
        try {
            localMember.setHostname(host);
        } catch (IOException e) {
            String msg = "Could not set the local member's name";
            log.error(msg, e);
            throw new ClusteringFault(msg, e);
        }
        Parameter localPort = getParameter(TribesConstants.LOCAL_MEMBER_PORT);
        int port;
        try {
            if (localPort != null) {
                port = Integer.parseInt(((String) localPort.getValue()).trim());
                port = getLocalPort(new ServerSocket(), localMember.getHostname(), port, 4000, 100);
            } else {
                port = getLocalPort(new ServerSocket(), localMember.getHostname(), -1, 4000, 100);
            }
        } catch (IOException e) {
            String msg = "Could not allocate the specified port or a port in the range 4000-4100 " + "for local host " + localMember.getHostname() + ". Check whether the IP address specified or inferred for the local " + "member is correct.";
            log.error(msg, e);
            throw new ClusteringFault(msg, e);
        }
        byte[] payload = "ping".getBytes();
        localMember.setPayload(payload);
        receiver.setPort(port);
        localMember.setPort(port);
        localMember.setDomain(localDomain);
        staticMembershipInterceptor.setLocalMember(localMember);
        for (Member member : members) {
            StaticMember tribesMember;
            try {
                tribesMember = new StaticMember(member.getHostName(), member.getPort(), 0, payload);
            } catch (IOException e) {
                String msg = "Could not add static member " + member.getHostName() + ":" + member.getPort();
                log.error(msg, e);
                throw new ClusteringFault(msg, e);
            }
            if (!(Arrays.equals(localMember.getHost(), tribesMember.getHost()) && localMember.getPort() == tribesMember.getPort())) {
                tribesMember.setDomain(localDomain);
                staticMembershipInterceptor.addStaticMember(tribesMember);
                primaryMembershipManager.addWellKnownMember(tribesMember);
                if (canConnect(member)) {
                    primaryMembershipManager.memberAdded(tribesMember);
                    log.info("Added static member " + TribesUtil.getName(tribesMember));
                } else {
                    log.info("Could not connect to member " + TribesUtil.getName(tribesMember));
                }
            }
        }
    }
