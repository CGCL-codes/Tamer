    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Please pass the name of the class to run as first parameter");
            System.exit(1);
        }
        String className = args[0];
        String[] newArgs = new String[args.length - 1];
        for (int i = 0; i < args.length - 1; i++) {
            newArgs[i] = args[i + 1];
        }
        String fullClassName = "org.hitchhackers.tools.jmx." + className;
        try {
            Class clazz = Class.forName(fullClassName);
            Object newInstance = clazz.newInstance();
            if (newInstance instanceof JMXClientBase) {
                try {
                    JMXClientBase theApplication = (JMXClientBase) newInstance;
                    theApplication.parse(newArgs);
                    theApplication.establishConnection();
                    theApplication.readParams();
                    theApplication.run();
                } catch (IllegalArgumentException e) {
                    System.err.println("ERROR : " + e.getMessage());
                    System.out.println("");
                    Method method;
                    try {
                        method = clazz.getDeclaredMethod("printUsage", new Class[0]);
                        method.invoke(null, new Object[0]);
                    } catch (Throwable t) {
                        System.err.println("There occurred an error, but the called class does not have usage information associated.");
                        System.err.println("Unfortunately I don't know how to help you...good luck!");
                        t.printStackTrace(System.err);
                    }
                    System.exit(1);
                } catch (IOException e) {
                    System.err.println("could not establish connection to VM via JMX : ");
                    e.printStackTrace(System.err);
                }
            } else {
                System.err.println("invalid class name '" + className + "' - I can work only with subclasses of JMXClientBase");
                System.exit(1);
            }
        } catch (ClassNotFoundException e) {
            System.err.println("could not find class '" + fullClassName + "'");
            e.printStackTrace(System.err);
            System.exit(2);
        } catch (InstantiationException e) {
            System.err.println("could not instantiate class '" + fullClassName + "' : ");
            e.printStackTrace(System.err);
        } catch (IllegalAccessException e) {
            System.err.println("could not access class '" + fullClassName + "' : ");
            e.printStackTrace(System.err);
        } catch (Throwable t) {
            System.err.println("There occurred an error: ");
            t.printStackTrace();
            System.exit(2);
        }
    }
