    public void update() {
        this.removeAllModules();
        if (this.modulePath == null) {
            throw new RuntimeException("The module path can not be null.");
        }
        String path = this.modulePath;
        File moduleDir = new File(path);
        if (!moduleDir.exists() || !moduleDir.isDirectory()) {
            int ans = JOptionPane.showConfirmDialog(null, "Create modules directory " + this.modulePath + "?", "Extension modules directory does not exist!", JOptionPane.YES_NO_CANCEL_OPTION);
            if (ans == JOptionPane.YES_OPTION) {
                try {
                    moduleDir.mkdir();
                } catch (SecurityException err) {
                    throw new RuntimeException("Problem creating and saving new configuration file.", err);
                }
            } else {
                return;
            }
        }
        File[] jarfiles = moduleDir.listFiles(new FileFilter() {

            public boolean accept(File f) {
                boolean out = false;
                if (f.getName().endsWith(".jar")) {
                    out = true;
                }
                return (out);
            }
        });
        if (jarfiles == null) {
            return;
        }
        if (jarfiles.length == 0) {
            return;
        }
        URL[] thisjarurl = new URL[1];
        URLClassLoader loader;
        for (int k = 0; k < jarfiles.length; k++) {
            ZipFile zip = null;
            try {
                zip = new ZipFile(jarfiles[k]);
            } catch (java.util.zip.ZipException err) {
                throw new IllegalArgumentException("Problem accessing jar file " + jarfiles[k] + ".", err);
            } catch (java.io.IOException err) {
                throw new IllegalArgumentException("Problem accessing jar file " + jarfiles[k] + ".", err);
            }
            Enumeration enumer = zip.entries();
            ZipEntry entry;
            try {
                thisjarurl[0] = new URL("file://" + jarfiles[k].getAbsolutePath());
            } catch (java.net.MalformedURLException err) {
                throw new IllegalArgumentException("Problem creating URL for file " + jarfiles[k] + ".", err);
            }
            loader = new URLClassLoader(thisjarurl);
            while (enumer.hasMoreElements()) {
                entry = (ZipEntry) enumer.nextElement();
                String name = entry.getName();
                if (name.endsWith(".class")) {
                    String classname = name.replace(".class", "");
                    classname = classname.replace("/", ".");
                    Class maybemodule = null;
                    try {
                        maybemodule = loader.loadClass(classname);
                    } catch (ClassNotFoundException err) {
                        throw new RuntimeException("Problem loading class from jar.", err);
                    }
                    Class[] intfces = maybemodule.getInterfaces();
                    if ((maybemodule != null) && (Module.class.isAssignableFrom(maybemodule))) {
                        Iterator iter = this.moduleClasses.iterator();
                        boolean filterPass = false;
                        while (iter.hasNext()) {
                            Class filter = (Class) iter.next();
                            if (filter.isAssignableFrom(maybemodule.getSuperclass())) {
                                filterPass = true;
                                break;
                            } else {
                            }
                        }
                        if (!filterPass) {
                            break;
                        }
                        Date d = new Date(entry.getTime());
                        String time = d.toString();
                        Module mod = null;
                        try {
                            mod = (Module) maybemodule.newInstance();
                        } catch (java.lang.InstantiationException err) {
                            throw new RuntimeException("Problem instantiating class " + classname + ".", err);
                        } catch (java.lang.IllegalAccessException err) {
                            throw new RuntimeException("Problem instantiating class " + classname + ".", err);
                        }
                        this.addModule(classname);
                        this.setDescription(classname, mod.getDescription());
                        this.setAuthor(classname, mod.getAuthor());
                        this.setCopyRight(classname, mod.getCopyRight());
                        this.setLastModified(classname, mod.getLastModified());
                        this.setVersion(classname, mod.getVersion());
                        this.setShortName(classname, mod.getShortName());
                        this.fireModuleAdded(classname);
                    } else {
                    }
                }
            }
        }
    }
