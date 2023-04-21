    private void connectAndLogin(SecurityAuthority authority) throws XMPPException, OperationFailedException {
        synchronized (initializationLock) {
            String password = JabberActivator.getProtocolProviderFactory().loadPassword(getAccountID());
            if (password == null) {
                UserCredentials credentials = new UserCredentials();
                credentials.setUserName(getAccountID().getUserID());
                credentials = authority.obtainCredentials(ProtocolNames.JABBER, credentials);
                if (credentials == null) {
                    fireRegistrationStateChanged(getRegistrationState(), RegistrationState.UNREGISTERED, RegistrationStateChangeEvent.REASON_USER_REQUEST, "");
                    return;
                }
                char[] pass = credentials.getPassword();
                if (pass == null) {
                    fireRegistrationStateChanged(getRegistrationState(), RegistrationState.UNREGISTERED, RegistrationStateChangeEvent.REASON_USER_REQUEST, "");
                    return;
                }
                password = new String(pass);
                if (credentials.isPasswordPersistent()) {
                    JabberActivator.getProtocolProviderFactory().storePassword(getAccountID(), password);
                }
            }
            try {
                String userID = StringUtils.parseName(getAccountID().getUserID());
                String serviceName = StringUtils.parseServer(getAccountID().getUserID());
                String serverAddress = (String) getAccountID().getAccountProperties().get(ProtocolProviderFactory.SERVER_ADDRESS);
                String serverPort = (String) getAccountID().getAccountProperties().get(ProtocolProviderFactory.SERVER_PORT);
                String accountResource = (String) getAccountID().getAccountProperties().get(ProtocolProviderFactory.RESOURCE);
                try {
                    String hosts[] = NetworkUtils.getSRVRecords("_xmpp-client._tcp." + serviceName);
                    if (hosts != null && hosts.length > 0) {
                        logger.trace("Will set server address from SRV records " + hosts[0]);
                        serverAddress = hosts[0];
                    }
                } catch (ParseException ex1) {
                    logger.error("Domain not resolved " + ex1.getMessage());
                }
                Roster.setDefaultSubscriptionMode(Roster.SubscriptionMode.manual);
                try {
                    ConnectionConfiguration confConn = new ConnectionConfiguration(serverAddress, Integer.parseInt(serverPort), serviceName);
                    connection = new XMPPConnection(confConn);
                    connection.connect();
                } catch (XMPPException exc) {
                    logger.error("Failed to establish a Jabber connection for " + getAccountID().getAccountUniqueID(), exc);
                    throw new OperationFailedException("Failed to establish a Jabber connection for " + getAccountID().getAccountUniqueID(), OperationFailedException.NETWORK_FAILURE, exc);
                }
                connection.addConnectionListener(new JabberConnectionListener());
                fireRegistrationStateChanged(getRegistrationState(), RegistrationState.REGISTERING, RegistrationStateChangeEvent.REASON_NOT_SPECIFIED, null);
                if (accountResource == null || accountResource == "") accountResource = "sip-comm";
                connection.login(userID, password, accountResource);
                if (connection.isAuthenticated()) {
                    this.reconnecting = false;
                    connection.getRoster().setSubscriptionMode(Roster.SubscriptionMode.manual);
                    fireRegistrationStateChanged(getRegistrationState(), RegistrationState.REGISTERED, RegistrationStateChangeEvent.REASON_NOT_SPECIFIED, null);
                } else {
                    fireRegistrationStateChanged(getRegistrationState(), RegistrationState.UNREGISTERED, RegistrationStateChangeEvent.REASON_NOT_SPECIFIED, null);
                }
            } catch (NumberFormatException ex) {
                throw new OperationFailedException("Wrong port", OperationFailedException.INVALID_ACCOUNT_PROPERTIES, ex);
            }
        }
        if (getRegistrationState() == RegistrationState.REGISTERED) {
            discoveryManager = ServiceDiscoveryManager.getInstanceFor(connection);
            discoveryManager.setIdentityName("sip-comm");
            discoveryManager.setIdentityType("registered");
            Iterator it = supportedFeatures.iterator();
            while (it.hasNext()) {
                discoveryManager.addFeature((String) it.next());
            }
        }
    }
