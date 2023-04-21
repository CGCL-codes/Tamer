        public void run() {
            this.xmpp.Conn = new XMPPConnection(this.xmpp.Config);
            try {
                this.xmpp.Conn.connect();
                this.xmpp.Conn.getSASLAuthentication().supportSASLMechanism("PLAIN", 0);
                this.xmpp.Conn.login(login, pass, resource);
            } catch (XMPPException e) {
                System.out.println("XMPPException: " + e.getMessage());
                this.xmpp.Error_message = e.getMessage();
                this.xmpp.After_Login(false);
                return;
            }
            this.xmpp.After_Login(true);
        }
