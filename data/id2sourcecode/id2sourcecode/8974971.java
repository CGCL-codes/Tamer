    public static void main(String[] args) throws Exception {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            LOGGING.log(Level.INFO, "System Look-and-Feel not loaded", e);
        }
        if (args.length == 0) {
            ClassLoader cl = JPCApplication.class.getClassLoader();
            if (cl instanceof URLClassLoader) {
                for (int i = 0; i < ((URLClassLoader) cl).getURLs().length; i++) {
                    URL url = ((URLClassLoader) cl).getURLs()[i];
                    InputStream in = url.openStream();
                    try {
                        JarInputStream jar = new JarInputStream(in);
                        Manifest manifest = jar.getManifest();
                        if (manifest == null) {
                            continue;
                        }
                        String defaultArgs = manifest.getMainAttributes().getValue("Default-Args");
                        if (defaultArgs == null) {
                            continue;
                        }
                        args = defaultArgs.split("\\s");
                        break;
                    } catch (IOException e) {
                        System.err.println("Not a JAR file " + url);
                    } finally {
                        try {
                            in.close();
                        } catch (IOException e) {
                        }
                    }
                }
            }
            if (args.length == 0) {
                LOGGING.log(Level.INFO, "No configuration specified, using defaults");
                args = DEFAULT_ARGS;
            } else {
                LOGGING.log(Level.INFO, "Using configuration specified in manifest");
            }
        } else {
            LOGGING.log(Level.INFO, "Using configuration specified on command line");
        }
        if (ArgProcessor.findVariable(args, "compile", "yes").equalsIgnoreCase("no")) PC.compile = false;
        String memarg = ArgProcessor.findVariable(args, "m", null);
        PC pc;
        if (memarg == null) pc = new PC(new VirtualClock(), args); else {
            int mem;
            if (memarg.endsWith("G") || memarg.endsWith("g")) mem = Integer.parseInt(memarg.substring(0, memarg.length() - 1)) * 1024 * 1024 * 1024; else if (memarg.endsWith("M") || memarg.endsWith("m")) mem = Integer.parseInt(memarg.substring(0, memarg.length() - 1)) * 1024 * 1024; else if (memarg.endsWith("K") || memarg.endsWith("k")) mem = Integer.parseInt(memarg.substring(0, memarg.length() - 1)) * 1024; else mem = Integer.parseInt(memarg.substring(0, memarg.length()));
            pc = new PC(new VirtualClock(), args, mem);
        }
        String net = ArgProcessor.findVariable(args, "net", "no");
        if (net.startsWith("hub:")) {
            int port = 80;
            String server;
            int index = net.indexOf(":", 5);
            if (index != -1) {
                port = Integer.parseInt(net.substring(index + 1));
                server = net.substring(4, index);
            } else server = net.substring(4);
            EthernetOutput hub = new EthernetHub(server, port);
            EthernetCard card = (EthernetCard) pc.getComponent(EthernetCard.class);
            card.setOutputDevice(hub);
        }
        final JPCApplication app = new JPCApplication(args, pc);
        app.setBounds(100, 100, MONITOR_WIDTH + 20, MONITOR_HEIGHT + 70);
        try {
            app.setIconImage(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("resources/icon.png")));
        } catch (Exception e) {
        }
        app.validate();
        app.setVisible(true);
    }
