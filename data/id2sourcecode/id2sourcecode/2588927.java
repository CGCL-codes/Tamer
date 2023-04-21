    public static void init(String[] args) {
        logger = LoggerManager.get("com.fujitsu.arcon.gateway.Configuration");
        boolean conf_ok = true;
        String home_name = System.getProperty("user.dir");
        if (args.length > 0) home_name = args[0];
        home = new File(home_name);
        if (!home.isDirectory()) {
            if (!home.exists()) {
                logger.severe("The directory for configuration files does not exist <" + home.getPath() + ">");
            } else {
                logger.severe("The configuration directory is not a directory <" + home.getPath() + ">");
            }
            conf_ok = false;
        } else if (!home.canRead()) {
            logger.severe("Cannot read the configuration directory <" + home.getPath() + ">");
            conf_ok = false;
        }
        if (!conf_ok) {
            logger.severe("The configuration directory name is the first argument.");
            TerminationManager.get().abort();
        }
        logger.config("Configuration directory is: " + home.getPath());
        String config_file_name = "gateway.properties";
        if (args.length == 2) config_file_name = args[1];
        File config_file = new File(config_file_name);
        if (!config_file.isAbsolute()) config_file = new File(home, config_file_name);
        if (!config_file.exists()) {
            logger.severe("The configuration file <" + config_file.getPath() + "> does not exist.");
            conf_ok = false;
        }
        if (!config_file.canRead()) {
            logger.severe("Cannot read the configuration file <" + config_file.getPath() + ">");
            conf_ok = false;
        }
        if (!conf_ok) {
            logger.severe("The configuration file name is the second argument.");
            TerminationManager.get().abort();
        }
        logger.config("Reading configuration from: " + config_file.getPath());
        Properties defaults = new Properties();
        try {
            defaults.load(new FileInputStream(config_file));
        } catch (IOException ex) {
            logger.severe("Problem with configuration file", ex);
            TerminationManager.get().abort();
        }
        Properties merged = new Properties(defaults);
        ByteArrayOutputStream out_props = new ByteArrayOutputStream();
        try {
            System.getProperties().store(out_props, "NJS Internal convert");
        } catch (Exception ex) {
        }
        try {
            merged.load(new ByteArrayInputStream(out_props.toByteArray()));
        } catch (IOException ex) {
        }
        System.setProperties(merged);
        watch_interval = getLong("file_watch_interval", 1000);
        logger.config("Check for command files every " + watch_interval + " milliseconds");
        unusedEntry("log_is");
        unusedEntry("debug_threads");
        unusedEntry("debug_connections");
        unusedEntry("log_directory");
        unusedEntry("flush_log");
        String value = getString("change_log_files", "");
        if (value.equals("")) {
            value = getString("change_log_int", "24");
        }
        try {
            nlfc = Long.parseLong(value);
            if (nlfc <= 0) {
                nlfc = 1;
                logger.warning("The value <change_log_files<=0> is no longer valid. Logging to file with hourly changes.");
            } else {
                logger.config("Change log files every " + nlfc + " hours");
            }
            nlfc *= 60 * 60 * 1000;
        } catch (Exception ex) {
            value = value.toLowerCase();
            if (value.startsWith("h")) {
                logger.config("Change log files on the hour.");
                nlfc = -1;
            } else if (value.startsWith("d")) {
                logger.config("Change log files on the day.");
                nlfc = -2;
            } else {
                logger.severe("Request for log file changing not understood (change_log_files or change_log_int): " + value);
                conf_ok = false;
            }
        }
        value = getString("logging_level", "C").toUpperCase().trim();
        char new_level = value.charAt(0);
        LoggerLevel ll = null;
        if (new_level == LoggerLevel.SEVERE.getToLog()) ll = LoggerLevel.SEVERE;
        if (new_level == LoggerLevel.WARNING.getToLog()) ll = LoggerLevel.WARNING;
        if (new_level == LoggerLevel.INFO.getToLog()) ll = LoggerLevel.INFO;
        if (new_level == LoggerLevel.CONFIG.getToLog()) ll = LoggerLevel.CONFIG;
        if (new_level == LoggerLevel.CHAT.getToLog()) ll = LoggerLevel.CHAT;
        if (new_level == LoggerLevel.DRIVEL.getToLog()) ll = LoggerLevel.DRIVEL;
        LoggerManager.setLevel(ll);
        logger.config("Logging level is set to " + new_level);
        String connections_file_name = getString("connections", "registered_vsites_only");
        unusedEntry("vsites");
        if (!connections_file_name.equals("registered_vsites_only")) {
            connections_file = new File(connections_file_name);
            if (!connections_file.isAbsolute()) {
                connections_file = new File(home, connections_file_name);
            }
            if (!connections_file.exists()) {
                logger.severe("The connections file <" + connections_file.getPath() + "> does not exist.");
                conf_ok = false;
            }
            if (!connections_file.canRead()) {
                logger.severe("Cannot read the connections file <" + connections_file.getPath() + ">");
                conf_ok = false;
            }
            logger.config("Reading Connection definitions (client, Vsites) from <" + connections_file.getPath() + ">");
        } else {
            logger.config("No definitions for Vsites, will rely on NJS registration");
        }
        if (!conf_ok) {
            logger.severe("The connections file is set using the configuration value <connections> or <vsites>");
            TerminationManager.get().abort();
        }
        incoming_port = (int) getLong("port", 4433);
        try {
            String given = getString("gateway_host_name", "NONE");
            if (!given.equals("NONE")) {
                url = "ssl://" + given + ":";
            } else if (getBoolean("return_host_address", false)) {
                url = "ssl://" + java.net.InetAddress.getLocalHost().getHostAddress() + ":";
            } else {
                url = "ssl://" + java.net.InetAddress.getLocalHost().getHostName() + ":";
            }
            logger.config("Gateway URL for all Vsites is: " + url);
        } catch (Exception ex) {
            logger.severe("Problems determining local host name", ex);
            TerminationManager.get().abort();
        }
        String identity_file_name = getString("identity", "");
        if (identity_file_name.equals("")) {
            identity_file_name = getString("server_cert", "server_cert");
        }
        identity_file = new File(identity_file_name);
        if (!identity_file.isAbsolute()) {
            identity_file = new File(home, identity_file_name);
        }
        trusted_cas = getString("trusted_cas", "trusted_cas");
        passwords = new HashMap();
        File passwd_file = new File(home, "password");
        if (passwd_file.exists()) {
            logger.config("Reading passwords from <" + passwd_file.getPath() + ">");
            try {
                BufferedReader passwd_in = new BufferedReader(new FileReader(passwd_file));
                String line = passwd_in.readLine();
                while (line != null) {
                    StringTokenizer st = new StringTokenizer(line);
                    String one = st.nextToken();
                    if (st.hasMoreTokens()) {
                        passwords.put(one, st.nextToken().toCharArray());
                        logger.config("Read a password for <" + one + "> from file.");
                    } else {
                        password = one.toCharArray();
                        logger.config("Read a general password from file.");
                    }
                    line = passwd_in.readLine();
                }
            } catch (Exception ex) {
                logger.severe("Problems reading the passwords.", ex);
            } finally {
                passwd_file.delete();
            }
        } else {
            logger.config("No password file found.");
        }
        String s_password = getString("password", null);
        if (s_password != null) {
            logger.config("Read a general password from configuration file.");
            password = s_password.toCharArray();
        }
        max_idle = (int) getLong("conn_timeout", 1);
        logger.config("Idle client connections will be dropped after <" + max_idle + "> minutes");
        max_idle *= 60 * 1000;
        check_crls = getBoolean("check_crls", false);
        logger.config("Will " + (check_crls ? "" : "not ") + "check incoming certificates against Certificate Revocation lists");
        if (check_crls) {
            crls_grace = getDouble("crls_grace_ratio", -1.0);
            if (crls_grace > 0.0) logger.config("Will allow <" + crls_grace + "> of CRL validity time as grace for failed loads");
            fallback_crl_location = getString("crl_fallback_location");
            if (fallback_crl_location != null) logger.config("Will use a CRL from <" + fallback_crl_location + "> if none in certificate");
            default_crl_validity = getLong("default_crl_validity", 24);
            logger.config("By default CRLs are valid for <" + default_crl_validity + "> hours");
            default_crl_validity *= 1000 * 60 * 60;
        }
        int max_threads = (int) getLong("max_threads", 100);
        logger.config("The Gateway will refuse incoming connections if there are more than <" + max_threads + "> active execution threads");
        ThreadManager.MAX_THREAD_NUMBER = max_threads;
        String named_njss = getString("named_njs", "no");
        if (named_njss.equals("no")) {
            VsiteManager.NAMED_ONLY = false;
        } else {
            VsiteManager.NAMED_ONLY = true;
            StringTokenizer st = new StringTokenizer(named_njss, ":");
            VsiteManager.ACCEPTABLE = new String[st.countTokens()];
            for (int i = 0; i < VsiteManager.ACCEPTABLE.length; i++) {
                VsiteManager.ACCEPTABLE[i] = st.nextToken().trim();
            }
        }
        if (!conf_ok) {
            logger.severe("There are problems with the configuration, cannot start");
            TerminationManager.get().abort();
        }
    }
