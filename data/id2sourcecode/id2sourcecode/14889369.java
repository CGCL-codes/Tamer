    public XMPPConnection connect() {
        ConnectionConfiguration connConfig = new ConnectionConfiguration(host, Integer.parseInt(port), service);
        XMPPConnection connection = new XMPPConnection(connConfig);
        try {
            connection.connect();
            Log.i("XMPPClient", "[SettingsDialog] Connected to " + connection.getHost());
        } catch (XMPPException ex) {
            String error = getString(R.string.xmpp_error_text_connection);
            Log.e("XMPPClient", error + " " + connection.getHost());
            Log.e("XMPPClient", ex.toString());
            serviceBinder.addErrorForObservation(incident.getParentId(), ErrorFacilities.XMPP, error + " " + connection.getHost());
            serviceBinder.addErrorForObservation(incident.getParentId(), ErrorFacilities.XMPP, ex.toString());
            return null;
        }
        try {
            connection.login(username, password);
            Log.i("XMPPClient", "Logged in as " + connection.getUser());
            Presence presence = new Presence(Presence.Type.available);
            connection.sendPacket(presence);
        } catch (XMPPException ex) {
            String error = getString(R.string.xmpp_error_text_login);
            Log.e("XMPPClient", error + " " + username);
            Log.e("XMPPClient", ex.toString());
            serviceBinder.addErrorForObservation(incident.getParentId(), ErrorFacilities.XMPP, error + " " + username);
            serviceBinder.addErrorForObservation(incident.getParentId(), ErrorFacilities.XMPP, ex.toString());
            return null;
        } catch (IllegalStateException e) {
            Log.e("XMPPClient", e.toString());
            return null;
        }
        return connection;
    }
