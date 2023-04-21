    static int execute(String[] args, java.io.InputStream reader, java.io.OutputStream writer, java.io.OutputStream error, java.lang.reflect.Method[] methodToCall) throws Exception {
        new URLConnection(Main.class.getResource("Main.class")) {

            public void connect() throws IOException {
            }
        }.setDefaultUseCaches(false);
        ArrayList list = new ArrayList();
        HashSet processedDirs = new HashSet();
        String home = System.getProperty("netbeans.home");
        if (home != null) {
            build_cp(new File(home), list, processedDirs);
        }
        String nbdirs = System.getProperty("netbeans.dirs");
        if (nbdirs != null) {
            StringTokenizer tok = new StringTokenizer(nbdirs, File.pathSeparator);
            while (tok.hasMoreTokens()) {
                build_cp(new File(tok.nextToken()), list, processedDirs);
            }
        }
        String prepend = System.getProperty("netbeans.classpath");
        if (prepend != null) {
            StringTokenizer tok = new StringTokenizer(prepend, File.pathSeparator);
            while (tok.hasMoreElements()) {
                list.add(0, new File(tok.nextToken()));
            }
        }
        StringBuffer buf = new StringBuffer(1000);
        Iterator it = list.iterator();
        while (it.hasNext()) {
            if (buf.length() > 0) {
                buf.append(File.pathSeparatorChar);
            }
            buf.append(((File) it.next()).getAbsolutePath());
        }
        System.setProperty("netbeans.dynamic.classpath", buf.toString());
        ListIterator it2 = list.listIterator();
        while (it2.hasNext()) {
            File f = (File) it2.next();
            if (f.isFile()) {
                it2.set(new JarFile(f, false));
            }
        }
        BootClassLoader loader = new BootClassLoader(list, new ClassLoader[] { Main.class.getClassLoader() });
        if (!Boolean.getBoolean("netbeans.use-app-classloader")) Thread.currentThread().setContextClassLoader(loader);
        CLIHandler.Status result;
        result = CLIHandler.initialize(args, reader, writer, error, loader, true, false, loader);
        if (result.getExitCode() == CLIHandler.Status.CANNOT_CONNECT) {
            int value = javax.swing.JOptionPane.showConfirmDialog(null, java.util.ResourceBundle.getBundle("org/netbeans/Bundle").getString("MSG_AlreadyRunning"), java.util.ResourceBundle.getBundle("org/netbeans/Bundle").getString("MSG_AlreadyRunningTitle"), javax.swing.JOptionPane.OK_CANCEL_OPTION, javax.swing.JOptionPane.WARNING_MESSAGE);
            if (value == javax.swing.JOptionPane.OK_OPTION) {
                result = CLIHandler.initialize(args, reader, writer, error, loader, true, true, loader);
            }
        }
        String className = System.getProperty("netbeans.mainclass", "org.netbeans.core.startup.Main");
        Class c = loader.loadClass(className);
        Method m = c.getMethod("main", new Class[] { String[].class });
        if (methodToCall != null) {
            methodToCall[0] = m;
        }
        return result.getExitCode();
    }
