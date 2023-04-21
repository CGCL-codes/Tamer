    private void loader(String[] args) {
        Vector dynamicClassPath = new Vector();
        String className = null;
        String[] passedArgs = null;
        boolean verbose = false;
        boolean systemClassPathLast = false;
        String loaderType = null;
        int i = 0;
        try {
            while (i < args.length) {
                if (args[i].equals("-loader")) {
                    loaderType = args[i + 1];
                    i++;
                } else if (args[i].equals("-verbose")) {
                    verbose = true;
                } else if (args[i].equals("-systemcplast")) {
                    systemClassPathLast = true;
                } else if (args[i].equals("-cpalljars")) {
                    File dir = new File(args[i + 1]);
                    if (dir.isDirectory()) dynamicClassPath.addAll(addRecursive(dir)); else {
                        System.err.println("Not a directory: " + dir.getAbsolutePath());
                        return;
                    }
                    i++;
                } else if (args[i].equals("-cpdir")) {
                    File dir = new File(args[i + 1]);
                    if (dir.isDirectory()) dynamicClassPath.add(dir); else {
                        System.err.println("Not a directory: " + dir.getAbsolutePath());
                        return;
                    }
                    i++;
                } else if (args[i].equals("-cpjar")) {
                    File jarfile = new File(args[i + 1]);
                    if (jarfile.isFile() && jarfile.canRead()) dynamicClassPath.add(jarfile); else {
                        System.err.println("Not a JAR file: " + jarfile.getAbsolutePath());
                        return;
                    }
                    i++;
                } else {
                    className = args[i];
                    i++;
                    passedArgs = new String[args.length - i];
                    System.arraycopy(args, i, passedArgs, 0, passedArgs.length);
                    break;
                }
                i++;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Required parameter missing for " + args[i]);
            help();
            return;
        }
        if (className == null) {
            System.err.println("You did not specify class name to be loaded.");
            help();
            return;
        }
        if (verbose) {
            System.out.println("Attempting to load class " + className + " using classpath repositories:");
            for (int j = 0; j < dynamicClassPath.size(); j++) {
                File repository = (File) dynamicClassPath.get(j);
                System.out.println("\t" + (repository.isDirectory() ? "(dir) " : "(jar) ") + repository);
            }
            System.out.println("System class loader used " + (systemClassPathLast ? "after" : "before") + " the dynamic class repositories.");
        }
        ClassLoader loader = getLoaderForClasspath(dynamicClassPath, systemClassPathLast, loaderType);
        if (verbose) {
            final long start = System.currentTimeMillis();
            Runtime.getRuntime().addShutdownHook(new Thread() {

                public void run() {
                    System.out.println("Execution time: " + java.text.MessageFormat.format("{0,number,#.###}", new Object[] { new Double((System.currentTimeMillis() - start) / 1000.0) }));
                }
            });
        }
        try {
            Thread.currentThread().setContextClassLoader(loader);
            Class invokeMeClass = loader.loadClass(className);
            Method main = invokeMeClass.getMethod("main", new Class[] { String[].class });
            int modifiers = main.getModifiers();
            if (Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers)) {
                main.invoke(invokeMeClass, new Object[] { passedArgs });
            } else throw new NoSuchMethodException();
        } catch (InvocationTargetException e) {
            System.out.println("[" + this.getClass().getName() + "] " + className + " has thrown an exception.");
            e.getTargetException().printStackTrace();
        } catch (NoSuchMethodException e) {
            System.err.println("Class " + className + " has no command line entry point method.");
        } catch (IllegalAccessException e) {
            System.err.println("Class " + className + " is not accessible: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Class " + className + " not found. Use -verbose to see repositories list.");
        }
    }
