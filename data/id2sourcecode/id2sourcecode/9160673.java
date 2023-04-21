        private State connecting() {
            State state = State.CONNECTING;
            ChatService.this.notifyStateObserver(state);
            if (connection.isAuthenticated() && connection.isConnected()) {
                state = State.CONNECTED;
                return state;
            }
            try {
                connection.connect();
                SASLAuthentication.supportSASLMechanism("PLAIN", 0);
                if (use_login_retry) {
                    connection.login(username + this.login_retry, password);
                } else {
                    connection.login(username, password);
                }
                if (connection.isAuthenticated() && connection.isConnected()) {
                    if (use_login_retry) {
                        use_login_retry = !use_login_retry;
                        username = username + this.login_retry;
                        this.login_retry = -1;
                        MainFrame.getInstance().getJStockOptions().setChatUsername(username);
                    }
                    this.connection.removeConnectionListener(connectionListener);
                    this.connection.addConnectionListener(connectionListener);
                    state = State.CONNECTED;
                }
            } catch (XMPPException ex) {
                log.error(null, ex);
                final XMPPError error = ex.getXMPPError();
                if (error != null) {
                    if (error.getCode() == 504) {
                        state = State.CONNECTING;
                    }
                } else {
                    state = State.ACCOUNT_CREATING;
                }
            }
            return state;
        }
