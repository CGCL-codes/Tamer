    public static void main(String[] args) {
        int result;
        int error = common_h.FALSE;
        int display_license = common_h.FALSE;
        int display_help = common_h.FALSE;
        is_core = true;
        if (args == null || args.length < 1) {
            blue.config_file = locations_h.DEFAULT_CONFIG_FILE;
            if (!new File(blue.config_file).exists()) error = common_h.TRUE;
        } else {
            Options options = new Options();
            options.addOption("h", "help", false, "Display Help");
            options.addOption("?", "help", false, "Display Help");
            options.addOption("v", "verify", false, "Reads all data in the configuration files and performs a basic verification/sanity check.  Always make sure you verify your config data before (re)starting Blue.");
            options.addOption("V", "version", false, "Display Version and License information.");
            options.addOption("s", "schedule", false, "Shows projected/recommended check scheduling information based on the current data in the configuration files.");
            options.addOption("d", "daemon", false, "Starts Blue in daemon mode (instead of as a foreground process). This is the recommended way of starting Blue for normal operation.");
            options.addOption("c", "console", false, "Starts an embedded Web Server(Jetty) so you can run in one process");
            options.addOption("i", "initialise", false, "Tells Blue to start with an extremely basic configuration so you can get Blue running in no time at all");
            options.addOption("p", "nagios_plugins", true, "Tell Blue where your Nagios Plugins are. This is needed to help deploy the basic configuration.");
            try {
                CommandLineParser parser = new PosixParser();
                CommandLine cmd = parser.parse(options, args);
                if (cmd.hasOption('?') || cmd.hasOption('h')) display_help = common_h.TRUE;
                if (cmd.hasOption('V')) display_license = common_h.TRUE;
                if (cmd.hasOption('v')) blue.verify_config = common_h.TRUE;
                if (cmd.hasOption('s')) blue.test_scheduling = common_h.TRUE;
                if (cmd.hasOption('d')) blue.daemon_mode = common_h.TRUE;
                if (cmd.hasOption('c')) blue.console_mode = true;
                if (cmd.hasOption('i')) blue.initialise_config = true;
                if (cmd.hasOption('p')) blue.nagios_plugins = cmd.getOptionValue('p').trim();
                args = cmd.getArgs();
                if ((args == null || args.length != 1) && !cmd.hasOption('i')) blue.config_file = locations_h.DEFAULT_CONFIG_FILE; else if (args != null && !cmd.hasOption('i')) blue.config_file = args[0];
            } catch (ParseException pe) {
                logger.error(pe.getMessage(), pe);
                error = common_h.TRUE;
            }
        }
        if (blue.daemon_mode == common_h.FALSE) {
            System.out.println("Blue Star, A Java Port of " + common_h.PROGRAM_VERSION);
            System.out.println("Copyright (c) 2006 BLUE (http://blue.sourceforge.net/)");
            System.out.println("Last Modified: " + common_h.PROGRAM_MODIFICATION_DATE);
            System.out.println("License: GPL");
        }
        if (display_license == common_h.TRUE) {
            System.out.println("This program is free software; you can redistribute it and/or modify");
            System.out.println("it under the terms of the GNU General Public License version 2 as");
            System.out.println("published by the Free Software Foundation.");
            System.out.println("This program is distributed in the hope that it will be useful,");
            System.out.println("but WITHOUT ANY WARRANTY; without even the implied warranty of");
            System.out.println("MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the");
            System.out.println("GNU General Public License for more details.");
            System.out.println("You should have received a copy of the GNU General Public License");
            System.out.println("along with this program; if not, write to the Free Software");
            System.out.println("Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.");
            System.exit(common_h.OK);
        }
        if (error == common_h.TRUE || display_help == common_h.TRUE) {
            System.out.println("Usage: java -jar blue-server.jar [option] <main_config_file>");
            System.out.println("");
            System.out.println("Options:");
            System.out.println("");
            System.out.println("  -v   Reads all data in the configuration files and performs a basic");
            System.out.println("       verification/sanity check.  Always make sure you verify your");
            System.out.println("       config data before (re)starting Blue.");
            System.out.println("");
            System.out.println("  -s   Shows projected/recommended check scheduling information based");
            System.out.println("       on the current data in the configuration files.");
            System.out.println("");
            System.out.println("  -d   Starts Blue in daemon mode (instead of as a foreground process).");
            System.out.println("       This is the recommended way of starting Blue for normal operation.");
            System.out.println("       This is legacy from original base, irrelevant currently in java.");
            System.out.println("");
            System.out.println("  -c   Experimental!  Starts the blue-console within the same server.");
            System.out.println("");
            System.out.println("  -i   Initialise Blue with an extremely basic monitoring configuration.");
            System.out.println("		This is designed to get you up and Running with Blue in no time at");
            System.out.println("		at all my showing you how Blue operates. Your new configuration will");
            System.out.println("       be stored in the etc/basic directory!");
            System.out.println("");
            System.out.println("  -p   Location of Nagios Plugins. Use this option to tell Blue where your");
            System.out.println("		Nagios plugins are installed. This is used to help build the initial");
            System.out.println("		basic configuration and is not a required option");
            System.out.println("");
            System.out.println("Visit the Blue website at http://blue.sourceforge.net/ for bug fixes, new");
            System.out.println("releases, online documentation, FAQs, information on subscribing to");
            System.out.println("the mailing lists, and commercial and contract support for Blue.");
            System.out.println("");
            System.exit(common_h.ERROR);
        }
        if (blue.initialise_config) {
            String installLocation = System.getProperty("user.dir");
            result = common_h.OK;
            try {
                logger.info("Copying requested Basic Configuration");
                File myFile = new File(installLocation + "/etc/basic/xml/templates");
                if (!myFile.exists()) myFile.mkdirs();
                myFile = new File(installLocation + "/etc/basic/blue.cfg");
                if (myFile.exists()) {
                    logger.info("Requested configuration file already exists. Aborting copying process!");
                    result = common_h.ERROR;
                }
                if (result == common_h.OK) result = utils.copyDirectory(new File(installLocation + "/etc/samples/basic/"), new File(installLocation + "/etc/basic"));
                if (result == common_h.OK) {
                    utils.replaceString("/usr/local/blue/", installLocation + "/", new File(installLocation + "/etc/basic/resource.cfg"));
                    utils.replaceString("/usr/local/nagios/libexec", blue.nagios_plugins + "/", new File(installLocation + "/etc/basic/resource.cfg"));
                    utils.replaceString("/usr/local/blue/", installLocation + "/", new File(installLocation + "/etc/basic/xml/macros.xml"));
                    utils.replaceString("/usr/local/nagios/libexec", blue.nagios_plugins + "/", new File(installLocation + "/etc/basic/xml/macros.xml"));
                    utils.replaceString("/usr/local/blue/", installLocation + "/", new File(installLocation + "/etc/basic/blue.cfg"));
                    utils.replaceString("/usr/local/blue/", installLocation + "/", new File(installLocation + "/etc/basic/xml/blue.xml"));
                    utils.replaceString("/usr/local/blue/", installLocation + "/", new File(installLocation + "/etc/basic/cgi.cfg"));
                    utils.replaceString("etc/cgi.cfg", "etc/basic/cgi.cfg", new File(installLocation + "/etc/webdefault.xml"));
                }
                result = utils.lockConfigTool();
                if (result != common_h.OK) {
                    logger.info("Bailing out due to Error in copying basic configuration Files!");
                    utils.cleanup();
                    System.exit(result);
                }
                blue.config_file = installLocation + "/etc/basic/blue.cfg";
            } catch (Exception e) {
                logger.fatal("Bailing out due to Error in copying basic configuration Files!");
                utils.cleanup();
                System.exit(common_h.ERROR);
            }
        }
        File file = new File(blue.config_file);
        if (!file.isAbsolute()) {
            blue.config_file = file.getAbsoluteFile().toString();
        }
        if (blue.verify_config == common_h.TRUE) {
            utils.reset_variables();
            System.out.println("Reading configuration data...");
            System.out.println();
            if ((result = config.read_main_config_file(blue.config_file)) == common_h.OK) {
                result = config.read_all_object_data(blue.config_file);
            }
            if (result != common_h.OK) {
                if (!blue.config_file.endsWith("blue.cfg")) {
                    System.out.println("***> The name of the main configuration file looks suspicious...");
                    System.out.println();
                    System.out.println("     Make sure you are specifying the name of the MAIN configuration file on");
                    System.out.println("     the command line and not the name of another configuration file.  The");
                    System.out.println("     main configuration file is typically '$BLUE_HOME/config/blue.cfg'");
                }
                System.out.println();
                System.out.println("***> One or more problems was encountered while processing the config files...");
                System.out.println("");
                System.out.println("     Check your configuration file(s) to ensure that they contain valid");
                System.out.println("     directives and data defintions.  If you are upgrading from a previous");
                System.out.println("     version of Nagios, you should be aware that some variables/definitions");
                System.out.println("     may have been removed or modified in this version.  Make sure to read");
                System.out.println("     the HTML documentation regarding the config files, as well as the");
                System.out.println("     'Whats New' section to find out what has changed.");
                System.out.println();
            } else {
                System.out.println("Running pre-flight check on configuration data...");
                System.out.println();
                result = config.pre_flight_check();
                if (result == common_h.OK) {
                    System.out.println();
                    System.out.println("Things look okay - No serious problems were detected during the pre-flight check");
                    System.out.println();
                } else {
                    System.out.println();
                    System.out.println("***> One or more problems was encountered while running the pre-flight check...");
                    System.out.println();
                    System.out.println();
                    System.out.println("     Check your configuration file(s) to ensure that they contain valid");
                    System.out.println("     directives and data defintions.  If you are upgrading from a previous");
                    System.out.println("     version of Nagios, you should be aware that some variables/definitions");
                    System.out.println("     may have been removed or modified in this version.  Make sure to read");
                    System.out.println("     the HTML documentation regarding the config files, as well as the");
                    System.out.println("     'Whats New' section to find out what has changed.");
                    System.out.println();
                }
            }
            utils.cleanup();
            config_file = null;
            System.exit(result);
        } else if (test_scheduling == common_h.TRUE) {
            utils.reset_variables();
            if ((result = config.read_main_config_file(config_file)) == common_h.OK) {
                result = config.read_all_object_data(config_file);
            }
            if (result != common_h.OK) System.out.println("***> One or more problems was encountered while reading configuration data..."); else if ((result = config.pre_flight_check()) != common_h.OK) System.out.println("***> One or more problems was encountered while running the pre-flight check...");
            if (result == common_h.OK) {
                events.init_timing_loop();
                events.display_scheduling_info();
            }
            utils.cleanup();
            System.exit(result);
        } else {
            File file_lock = null;
            do {
                utils.reset_variables();
                program_start = utils.currentTimeInSeconds();
                macro_x[blue_h.MACRO_PROCESSSTARTTIME] = "" + program_start;
                result = config.read_main_config_file(config_file);
                nebmods.neb_init_modules();
                nebmods.neb_init_callback_list();
                logger.info("Blue " + common_h.PROGRAM_VERSION + " starting... (PID=" + blue_pid + ")");
                nebmods.neb_load_all_modules();
                broker.broker_program_state(broker_h.NEBTYPE_PROCESS_PRELAUNCH, broker_h.NEBFLAG_NONE, broker_h.NEBATTR_NONE, null);
                if (result == common_h.OK) result = config.read_all_object_data(config_file);
                if (result != common_h.OK) {
                    logger.fatal("Bailing out due to one or more errors encountered in the configuration files.  Run Blue from the command line with the -v option to verify your config before restarting. (PID=UNKNOWN)\n");
                    if (sigrestart == common_h.TRUE) utils.close_command_file();
                    broker.broker_program_state(broker_h.NEBTYPE_PROCESS_SHUTDOWN, broker_h.NEBFLAG_PROCESS_INITIATED, broker_h.NEBATTR_SHUTDOWN_ABNORMAL, null);
                    utils.cleanup();
                    System.exit(common_h.ERROR);
                }
                result = config.pre_flight_check();
                if (result != common_h.OK) {
                    logger.fatal("Bailing out due to errors encountered while running the pre-flight check.  Run Nagios from the command line with the -v option to verify your config before restarting. (PID=UNKNOWN)");
                    if (sigrestart == common_h.TRUE) utils.close_command_file();
                    broker.broker_program_state(broker_h.NEBTYPE_PROCESS_SHUTDOWN, broker_h.NEBFLAG_PROCESS_INITIATED, broker_h.NEBATTR_SHUTDOWN_ABNORMAL, null);
                    utils.cleanup();
                    System.exit(common_h.ERROR);
                }
                logger.info("Pre-flight checks passed successfully!");
                result = utils.lock_file_exists();
                if (result == common_h.OK) {
                    logger.fatal("Bailing out due to another version of Blue running.");
                    if (sigrestart == common_h.TRUE) utils.close_command_file();
                    broker.broker_program_state(broker_h.NEBTYPE_PROCESS_SHUTDOWN, broker_h.NEBFLAG_PROCESS_INITIATED, broker_h.NEBATTR_SHUTDOWN_ABNORMAL, null);
                    utils.cleanup();
                    System.exit(common_h.ERROR);
                }
                utils.setup_sighandler();
                broker.broker_program_state(broker_h.NEBTYPE_PROCESS_START, broker_h.NEBFLAG_NONE, broker_h.NEBATTR_NONE, null);
                try {
                    file_lock = new File(blue.lock_file);
                    blue_file_lock_channel = new RandomAccessFile(file_lock, "rw").getChannel();
                    blue_file_lock = blue_file_lock_channel.tryLock();
                    logger.info("Lock File LOCKED");
                } catch (Exception e) {
                    logger.fatal("Lock File FAILED");
                }
                result = utils.open_command_file();
                if (result != common_h.OK) {
                    logger.fatal("Bailing out due to errors encountered while trying to initialize the external command file... (PID= UNKNOWN)\n");
                    broker.broker_program_state(broker_h.NEBTYPE_PROCESS_SHUTDOWN, broker_h.NEBFLAG_PROCESS_INITIATED, broker_h.NEBATTR_SHUTDOWN_ABNORMAL, null);
                    utils.cleanup();
                    System.exit(common_h.ERROR);
                }
                if (blue.console_mode) {
                    try {
                        logger.info("Blue Embedded Console " + common_h.PROGRAM_VERSION + " starting... (PID=" + blue_pid + ")");
                        String path = new File(blue.config_file).getParent();
                        org.mortbay.start.Main.main(new String[] { path + "/jetty.xml" });
                    } catch (Exception e) {
                        logger.fatal("Bailing out could not launch embedded console.", e);
                        utils.cleanup();
                        System.exit(common_h.ERROR);
                    }
                }
                statusdata.initialize_status_data(config_file);
                comments.initialize_comment_data(config_file);
                downtime.initialize_downtime_data(config_file);
                perfdata.initialize_performance_data(config_file);
                sretention.read_initial_state_information(config_file);
                events.init_timing_loop();
                statusdata.update_all_status_data();
                logging.log_host_states(blue_h.INITIAL_STATES);
                logging.log_service_states(blue_h.INITIAL_STATES);
                try {
                    blue.ipc_pipe = Pipe.open();
                } catch (IOException ioE) {
                    logger.fatal("Error: Could not initialize service check IPC pipe...");
                    broker.broker_program_state(broker_h.NEBTYPE_PROCESS_SHUTDOWN, broker_h.NEBFLAG_PROCESS_INITIATED, broker_h.NEBATTR_SHUTDOWN_ABNORMAL, null);
                    utils.cleanup();
                    System.exit(common_h.ERROR);
                }
                result = service_result_worker_thread.init_service_result_worker_thread();
                if (result != common_h.OK) {
                    logger.fatal("Bailing out due to errors encountered while trying to initialize service result worker thread... (PID=UNKNONW)\n");
                    broker.broker_program_state(broker_h.NEBTYPE_PROCESS_SHUTDOWN, broker_h.NEBFLAG_PROCESS_INITIATED, broker_h.NEBATTR_SHUTDOWN_ABNORMAL, null);
                    utils.cleanup();
                    System.exit(common_h.ERROR);
                }
                sigrestart = common_h.FALSE;
                broker.broker_program_state(broker_h.NEBTYPE_PROCESS_EVENTLOOPSTART, broker_h.NEBFLAG_NONE, broker_h.NEBATTR_NONE, null);
                events.event_execution_loop();
                broker.broker_program_state(broker_h.NEBTYPE_PROCESS_EVENTLOOPEND, broker_h.NEBFLAG_NONE, broker_h.NEBATTR_NONE, null);
                if (sigshutdown == common_h.TRUE) broker.broker_program_state(broker_h.NEBTYPE_PROCESS_SHUTDOWN, broker_h.NEBFLAG_USER_INITIATED, broker_h.NEBATTR_SHUTDOWN_NORMAL, null); else if (sigrestart == common_h.TRUE) broker.broker_program_state(broker_h.NEBTYPE_PROCESS_RESTART, broker_h.NEBFLAG_USER_INITIATED, broker_h.NEBATTR_RESTART_NORMAL, null);
                sretention.save_state_information(config_file, common_h.FALSE);
                statusdata.cleanup_status_data(config_file, common_h.TRUE);
                comments.cleanup_comment_data(config_file);
                downtime.cleanup_downtime_data(config_file);
                perfdata.cleanup_performance_data(config_file);
                try {
                    ipc_pipe.sink().close();
                    ipc_pipe.source().close();
                } catch (IOException ioE) {
                    logger.error(ioE.getMessage(), ioE);
                }
                if (sigrestart == common_h.FALSE) utils.close_command_file();
                service_result_worker_thread.shutdown_service_result_worker_thread();
                if (sigshutdown == common_h.TRUE) {
                    logger.info("Successfully shutdown... (PID=UNKNOWN)");
                }
                utils.cleanup();
            } while (sigrestart == common_h.TRUE && sigshutdown == common_h.FALSE);
            try {
                logger.info("Releasing LOCK file!");
                blue_file_lock.release();
                blue_file_lock_channel.close();
            } catch (Exception e) {
            }
        }
    }
