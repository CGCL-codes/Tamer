    public void connectReal() {
        try {
            connection = getNewConnection();
            connection.connect();
            System.out.println("SMACK VERSION: " + SmackConfiguration.getVersion());
            setAuthenticationMethod();
            addListeners();
            connection.login(getUserName(), getPassword());
            fireConnect();
        } catch (XMPPException e) {
            log.log(Level.INFO, "", e);
            disconnect(false);
            String error = e.getXMPPError() == null ? e.getMessage() : e.getXMPPError().getMessage();
            error = error == null ? "" : error;
            notifyConnectionFailed("Connection Failed. " + error);
        } catch (RuntimeException e) {
            log.log(Level.SEVERE, "UNCAUGHT EXCEPTION! PLEASE FIX!", e);
            disconnect(false);
            notifyConnectionFailed("Connection Failed. UNUSUAL TERMINATION!" + e.getMessage());
        }
    }
