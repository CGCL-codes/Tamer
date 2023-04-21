    public Configuration(String configuration_directory, String configuration_file_name) {
        logger = LoggerManager.get("Configuration");
        String njs_home;
        if (configuration_directory != null) {
            njs_home = configuration_directory;
        } else {
            njs_home = getProperty("NJS_HOME", ".");
        }
        File njs_home_file = new File(njs_home);
        try {
            if (!njs_home_file.isDirectory()) {
                logger.severe("NJS home is not a directory <" + njs_home_file + ">");
                NJSGlobals.goToLimbo();
            }
            if (!njs_home_file.canRead()) {
                logger.severe("Cannot read NJS home directory <" + njs_home_file + ">");
                NJSGlobals.goToLimbo();
            }
            if (!njs_home_file.canWrite()) {
                logger.warning("Cannot write NJS home directory <" + njs_home_file + ">, NJS administration may not work");
            }
            NJSGlobals.setNJSHome(njs_home_file);
        } catch (Exception ex) {
            logger.severe("Problems checking NJS home directory <" + njs_home_file + ">");
            NJSGlobals.goToLimbo();
        }
        File njs_configuration_file;
        if (configuration_file_name != null) {
            njs_configuration_file = NJSGlobals.getFullPathFile(configuration_file_name);
        } else {
            njs_configuration_file = NJSGlobals.getFullPathFile("njs.properties");
        }
        Properties defaults = new Properties();
        try {
            defaults.load(new FileInputStream(njs_configuration_file));
        } catch (IOException ex) {
            logger.severe("Problem reading the NJS configuration file:" + njs_configuration_file, ex);
            NJSGlobals.goToLimbo();
        }
        Properties merged = new Properties(defaults);
        ByteArrayOutputStream out_props = new ByteArrayOutputStream();
        try {
            System.getProperties().store(out_props, "NJS Internal convert");
        } catch (IOException e) {
        }
        try {
            merged.load(new ByteArrayInputStream(out_props.toByteArray()));
        } catch (IOException ex) {
        }
        System.setProperties(merged);
        String rqd_level = getProperty("njs.logging_level", "Config");
        LoggerLevel rqd;
        if (rqd_level.equalsIgnoreCase("I")) {
            rqd = LoggerLevel.CONFIG;
        } else {
            rqd = LoggerLevel.convert(rqd_level);
            if (rqd == null) {
                rqd = LoggerLevel.CONFIG;
                logger.config("Unknown logging level, changing to CONFIG");
            }
        }
        logger.config("Initial logging at: " + rqd);
        LoggerManager.setLevel(rqd);
        logger.config("Reading NJS configuration from: " + njs_configuration_file);
        deprecatedEntry("njs.logdir");
        deprecatedEntry("NJS_DIR");
        unusedEntry("njs.logtimes");
        unusedEntry("njs.interactive");
        logger.config("NJS home directory: " + njs_home_file);
        NJSGlobals.setVsite(new org.unicore.Vsite(getProperty("njs.vsite_name", "no name for Vsite")));
        logger.config("Vsite name: " + NJSGlobals.getVsite().getName());
        NJSGlobals.getVsite().setType(org.unicore.Vsite.Type.NJS);
        NJSGlobals.setMissal(getProperty("njs.incarnationdb", "incarnationdb"));
        logger.config("Incarnation DB: " + NJSGlobals.getMissal());
        long mu = getLong("njs.tsi_update_interval", 5000);
        logger.config("NJS updates queues from TSIs: " + mu + " milliseconds");
        NJSGlobals.setUpdateFrequency(mu);
        int pl = getInt("njs.tsi_worker_limit", 5);
        if (pl < 2) pl = 2;
        logger.config("NJS can ask for the creation of up to " + pl + " TSI workers");
        com.fujitsu.arcon.njs.priest.ClassicTSIConnectionFactory.TSI_PROCESS_LIMIT = pl;
        NJSGlobals.setUseSSL(getBooleanProperty("njs.use_ssl", true));
        if (NJSGlobals.useSSL()) {
            String s = getProperty("njs.njs_cert_loc", "njs_cert.p12");
            NJSGlobals.setNJSCertLoc(s);
            s = getProperty("njs.unicore_ca_loc", "njs_ca.pem");
            NJSGlobals.setUnicoreCACertLoc(s);
        } else {
            logger.config("Not using SSL");
        }
        unusedEntry("njs.test_mode");
        String value = getProperty("njs.log_file_change_interval", "24");
        long nlfc;
        try {
            nlfc = Long.parseLong(value);
            if (nlfc <= 0) {
                nlfc = 1;
                logger.warning("The value <njs.log_file_change_interval<=0> is no longer valid. Logging to file with hourly changes.");
            } else {
                logger.config("Change log files every " + nlfc + " hours");
            }
            LoggerManager.setLogFileChangeInterval(nlfc);
        } catch (Exception ex) {
            value = value.toLowerCase();
            if (value.startsWith("h")) {
                logger.config("Change log files on the hour.");
                LoggerManager.setChangeLogFileOnTheHour();
            } else if (value.startsWith("d")) {
                logger.config("Change log files on the day.");
                LoggerManager.setChangeLogFileOnTheDay();
            } else {
                logger.severe("Request for log file changing not understood: " + value);
                NJSGlobals.goToLimbo();
            }
        }
        String s = System.getProperty("njs.ssl_password", "none");
        NJSGlobals.setPassword(s);
        s = getProperty("njs.trustedNJSs", "NONE");
        if (!s.equals("NONE")) NJSGlobals.setTrustedNJSFile(s);
        unusedEntry("njs.gw_conns_to");
        int port = getInt("njs.admin_port", 7272);
        logger.config("NJS listens for Administrators on port: " + port);
        NJSGlobals.setAdminPort(port);
        String atype = getProperty("njs.admin_type", "default");
        NJSGlobals.setAdminType(atype);
        String dir = getProperty("njs.save_dir", "save");
        NJSGlobals.setAJOSaveDir(dir);
        logger.config("NJS will save AJOs to <" + NJSGlobals.getAJOSaveDir() + ">");
        NJSGlobals.setSaveCompletedAJOs(getBooleanProperty("njs.save_completed_ajos", false));
        logger.config("NJS  will " + (NJSGlobals.getSaveCompletedAJOs() ? "not " : "") + " delete completed AJOs");
        if (getProperty("njs.operation_mode", "full").toLowerCase().equals("broker")) {
            logger.config("NJS is BROKER only");
            NJSGlobals.setBrokerOnly(true);
            NJSGlobals.getVsite().setType(new org.unicore.Vsite.Type("Broker"));
        } else {
            String sso_type = getProperty("njs.sso_type", null);
            if (sso_type != null) {
                if (sso_type.equalsIgnoreCase("globus")) {
                    logger.config("Will write SSOs to \".proxy\" in Uspaces");
                    NJSGlobals.setWriteProxiesToUspace(true);
                    NJSGlobals.getVsite().setType(new org.unicore.Vsite.Type("Globus"));
                } else if (sso_type.equalsIgnoreCase("gridftp_proxy") || sso_type.equalsIgnoreCase("gsi_proxy")) {
                    logger.config("Will write SSOs to \".proxy\" in Uspaces (for AFT GridFTP).");
                    NJSGlobals.setWriteProxiesToUspace(true);
                } else {
                    logger.severe("This NJS cannot handle SSOs of type <" + sso_type + ">");
                    NJSGlobals.goToLimbo();
                }
            }
        }
        String dr_file = getProperty("njs.dynamic_resource_file", null);
        if (dr_file != null) {
            logger.config("Will poll <" + NJSGlobals.getFullPathFile(dr_file) + "> for resource updates (every 60 seconds)");
            new com.fujitsu.arcon.njs.utils.GetDynamicResources(NJSGlobals.getFullPathFile(dr_file), 60000);
        }
        int buffer_size = getInt("njs.transfer_buffer_size", 16384);
        if (buffer_size <= 0) {
            logger.severe("File transfer buffer size (njs.transfer_buffer_size) should be positive, is <" + buffer_size + ">");
            NJSGlobals.goToLimbo();
        } else {
            NJSGlobals.setBufferSize(buffer_size);
            logger.config("File transfers will use buffers of size <" + buffer_size + ">");
        }
        long bss_job_complete_retries = getLong("njs.bss_job_completion_retries", 0);
        if (bss_job_complete_retries > 0) {
            logger.config("NJS will try to get a definite final job status <" + bss_job_complete_retries + "> times.");
            NJSGlobals.setBSSCompletionTestLimit(bss_job_complete_retries);
        }
        long ajo_retention_time = getLong("njs.ajo_retention_time", 0);
        if (ajo_retention_time > 0) {
            logger.config("NJS will keep AJOs for at most <" + ajo_retention_time + "> days.");
            NJSGlobals.setAJORetentionTime(ajo_retention_time * 60 * 60 * 24 * 1000);
        }
        String reservation_class_name = getProperty("njs.reservation_service_class", null);
        if (reservation_class_name == null) {
            DoGetResources.setHaveReservationService(false);
            reservation_class_name = "com.fujitsu.arcon.njs.utils.NullReservationService";
            logger.config("No resource reservation service.");
        } else {
            logger.config("Using class <" + reservation_class_name + "> for resource reservation service");
            DoGetResources.setHaveReservationService(true);
        }
        Class reservation_service_class = null;
        try {
            reservation_service_class = Class.forName(reservation_class_name);
        } catch (ClassNotFoundException ex) {
            logger.severe("Resource reservation service class <" + reservation_class_name + "> could not be found.\n" + "CLASSPATH OK? The class name fully qualified?", ex);
            NJSGlobals.goToLimbo();
        }
        ResourceReservationService reservation_service = null;
        try {
            reservation_service = (ResourceReservationService) reservation_service_class.newInstance();
            reservation_service.init((new NJSServicesOldStyleImpl.ResourceReservationServiceImpl()));
        } catch (Exception ex) {
            logger.severe("Problem creating an instance of the Resource reservation service class <" + reservation_class_name + ">", ex);
            NJSGlobals.goToLimbo();
        }
        NJSGlobals.setResourceReservationService(reservation_service);
        String reservation_execution_class_name = getProperty("njs.reservation_execution_class", null);
        if (reservation_execution_class_name == null) {
            logger.config("Do not expect requests to exeute Resource Reservation operations remotely.");
        } else {
            logger.config("Using class <" + reservation_execution_class_name + "> for resource reservation EXECUTION service");
            Class reservation_execution_class = null;
            try {
                reservation_execution_class = Class.forName(reservation_execution_class_name);
            } catch (ClassNotFoundException ex) {
                logger.severe("Resource reservation EXECUTION class <" + reservation_execution_class_name + "> could not be found.\n" + "CLASSPATH OK? The class name fully qualified?", ex);
                NJSGlobals.goToLimbo();
            }
            try {
                ResourceReservationExecution reservation_execution_service = (ResourceReservationExecution) reservation_execution_class.newInstance();
                reservation_execution_service.init((new NJSServicesOldStyleImpl.ResourceReservationExecutionImpl()));
            } catch (Exception ex) {
                logger.severe("Problem creating an instance of the Resource reservation EXECUTION class <" + reservation_execution_class_name + ">", ex);
                NJSGlobals.goToLimbo();
            }
        }
        String ajo_rewriter_class_name = getProperty("njs.ajo_rewriter_class", null);
        if (ajo_rewriter_class_name == null) {
            ajo_rewriter_class_name = "com.fujitsu.arcon.njs.utils.NullAJORewriter";
            logger.config("No AJO rewriting service.");
        } else {
            logger.config("Using class <" + ajo_rewriter_class_name + "> for AJO rewriting service");
        }
        Class ajo_rewriter_class = null;
        try {
            ajo_rewriter_class = Class.forName(ajo_rewriter_class_name);
        } catch (ClassNotFoundException ex) {
            logger.severe("AJO rewriter service class <" + ajo_rewriter_class_name + "> could not be found.\n" + "CLASSPATH OK? The class name fully qualified?", ex);
            NJSGlobals.goToLimbo();
        }
        AJORewriter ajo_rewriter_service = null;
        try {
            ajo_rewriter_service = (AJORewriter) ajo_rewriter_class.newInstance();
            ajo_rewriter_service.init((new NJSServicesImpl.AJORewriterServiceImpl()));
        } catch (Exception ex) {
            logger.severe("Problem creating an instance of the AJO rewriter service class <" + reservation_class_name + ">", ex);
            NJSGlobals.goToLimbo();
        }
        NJSGlobals.setAJORewriter(ajo_rewriter_service);
        NJSGlobals.setKeepUspaceAfterDone(getBooleanProperty("njs.keep_uspace_after_done", false));
        logger.config("Will delete Uspace " + (NJSGlobals.isKeepUspaceAfterDone() ? "when job is removed from NJS" : "when job execution has finished"));
        NJSGlobals.setPurgeTSIConnections(getBooleanProperty("njs.purge_tsi", true));
        logger.config("Will " + (NJSGlobals.getPurgeTSIConnections() ? "" : "not") + " regularly purge TSI connections (zombie management)");
        NJSGlobals.setmaxOutcomeInstances(getInt("njs.max_outcome_instances", 250));
        logger.config("Will keep at most the Outcomes of <" + NJSGlobals.getMaxOutcomeInstances() + "> loop iterations.");
        int client_port = getInt("njs.client_connections_port", -1);
        if (client_port > 0) {
            NJSGlobals.setListenForClientsPort(client_port);
        }
    }
