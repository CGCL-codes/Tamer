    public static Vector find(String pckgname, Class tosubclass) {
        Vector result;
        result = new Vector();
        String name = new String(pckgname);
        if (!name.startsWith("/")) {
            name = "/" + name;
        }
        name = name.replace('.', '/');
        StringTokenizer tok = new StringTokenizer(System.getProperty("java.class.path"), System.getProperty("path.separator"));
        while (tok.hasMoreTokens()) {
            String part = tok.nextToken();
            URL url = getURL(part, name);
            if (url == null) continue;
            File directory = new File(url.getFile());
            if (directory.exists()) {
                String[] files = directory.list();
                for (int i = 0; i < files.length; i++) {
                    if (files[i].endsWith(".class")) {
                        String classname = files[i].substring(0, files[i].length() - 6);
                        try {
                            Class cls = Class.forName(pckgname + "." + classname);
                            if (VERBOSE) System.out.println("- Checking: " + classname);
                            if (!Modifier.isAbstract(cls.getModifiers()) && !cls.isPrimitive()) {
                                if ((!tosubclass.isInterface() && isSubclass(tosubclass, cls)) || (tosubclass.isInterface() && hasInterface(tosubclass, cls))) {
                                    if (!result.contains(cls.getName())) {
                                        if (VERBOSE) System.out.println("- Added: " + classname);
                                        result.add(cls.getName());
                                    }
                                }
                            }
                        } catch (ClassNotFoundException cnfex) {
                            System.err.println(cnfex);
                        }
                    }
                }
            } else {
                try {
                    JarURLConnection conn = (JarURLConnection) url.openConnection();
                    String starts = conn.getEntryName();
                    JarFile jfile = conn.getJarFile();
                    Enumeration e = jfile.entries();
                    while (e.hasMoreElements()) {
                        ZipEntry entry = (ZipEntry) e.nextElement();
                        String entryname = entry.getName();
                        if (entryname.startsWith(starts) && (entryname.lastIndexOf('/') <= starts.length()) && entryname.endsWith(".class")) {
                            String classname = entryname.substring(0, entryname.length() - 6);
                            if (classname.startsWith("/")) classname = classname.substring(1);
                            classname = classname.replace('/', '.');
                            try {
                                Class cls = Class.forName(classname);
                                if (VERBOSE) System.out.println("- Checking: " + classname);
                                if (!Modifier.isAbstract(cls.getModifiers()) && !cls.isPrimitive()) {
                                    if ((!tosubclass.isInterface() && isSubclass(tosubclass, cls)) || (tosubclass.isInterface() && hasInterface(tosubclass, cls))) {
                                        if (!result.contains(cls.getName())) {
                                            if (VERBOSE) System.out.println("- Added: " + classname);
                                            result.add(cls.getName());
                                        }
                                    }
                                }
                            } catch (ClassNotFoundException cnfex) {
                                System.err.println(cnfex);
                            }
                        }
                    }
                } catch (IOException ioex) {
                    System.err.println(ioex);
                }
            }
        }
        RTSI r = new RTSI();
        Collections.sort(result, r.new StringCompare());
        return result;
    }
