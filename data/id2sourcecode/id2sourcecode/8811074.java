    public static void runSimulation(CommandLineOptions options, Properties properties, RemoteJist.JistClientRemote remote, PrintStream serverOut, RemoteJist.PingRemote ping) {
        try {
            if (properties != null) {
                Logger.getRootLogger().setLevel(Level.OFF);
                PropertyConfigurator.configure(properties);
            } else {
                BasicConfigurator.configure();
                Logger.getRootLogger().setLevel(Level.OFF);
            }
            if (options.bsh || options.jpy || options.sim != null) {
                String cachedir = options.nocache ? null : System.getProperty("java.io.tmpdir");
                Rewriter rewriter = new Rewriter(null, cachedir, remote, serverOut);
                Thread.currentThread().setContextClassLoader(rewriter);
                Controller controller = Controller.newController(rewriter);
                if (options.bsh) {
                    Bootstrap.create(JistAPI.RUN_BSH, controller, options.sim, options.args, null);
                } else if (options.jpy) {
                    Bootstrap.create(JistAPI.RUN_JPY, controller, options.sim, options.args, null);
                } else if (options.sim != null) {
                    Bootstrap.create(JistAPI.RUN_CLASS, controller, options.sim, options.args, null);
                }
                if (options.logger != null) {
                    controller.setLog(Class.forName(options.logger, true, rewriter));
                }
                try {
                    controller.start();
                    Thread t = startClientPingThread(ping, controller);
                    controller.join();
                    if (t != null) {
                        t.interrupt();
                        while (t.isAlive()) {
                            try {
                                Thread.sleep(50);
                            } catch (InterruptedException e) {
                            }
                        }
                    }
                } finally {
                    Throwable t = controller.reset();
                    if (t != null) {
                        if (t instanceof VirtualMachineError) {
                            throw (VirtualMachineError) t;
                        } else {
                            t.printStackTrace();
                        }
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            System.out.println("Simulation class not found: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
