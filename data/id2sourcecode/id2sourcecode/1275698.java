    protected void processRequest(ListenerRequest listenerRequest) throws IOException, RadiusException {
        Socket socket = listenerRequest.getSocket();
        socket.setSoTimeout(20000);
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String userName = reader.readLine();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        String realmName = realmFromUserName(userName);
        JRadiusRealm realm = JRadiusRealmManager.get(realmName);
        if (realm == null) throw new OTPProxyException("no such realm: " + realmName);
        OTPProxyRequest request = new OTPProxyRequest(userName, realm, socket, reader, writer);
        request.start();
        put(request);
    }
