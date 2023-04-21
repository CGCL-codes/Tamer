    public static void main(String[] args) {
        try {
            if (args.length == 0) {
                ClassLoader cl = PC.class.getClassLoader();
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
                    args = new String[] { "-fda", "mem:resources/images/floppy.img", "-hda", "mem:resources/images/dosgames.img", "-boot", "fda" };
                } else {
                    LOGGING.log(Level.INFO, "Using configuration specified in manifest");
                }
            } else {
                LOGGING.log(Level.INFO, "Using configuration specified on command line");
            }
            if (ArgProcessor.findVariable(args, "compile", "yes").equalsIgnoreCase("no")) {
                compile = false;
            }
            PC pc = new PC(new VirtualClock(), args);
            pc.start();
            try {
                while (true) {
                    pc.execute();
                }
            } finally {
                pc.stop();
                LOGGING.log(Level.INFO, "PC Stopped");
                pc.getProcessor().printState();
            }
        } catch (IOException e) {
            System.err.println("IOError starting PC");
        }
    }
