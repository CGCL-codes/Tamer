        private void doConnect() throws Exception {
            log.entry();
            log.debug("Beginning connection...");
            bncs.connect(new InetSocketAddress(host, port)).get();
            log.debug("Connection complete.");
            log.debug("Beginning login...");
            bncs.login(user, pass, pvpgn).get();
            log.debug("Login complete.");
            log.exit();
        }
