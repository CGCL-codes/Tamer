    public static void find(String pckgname, Class<?> tosubclass) {
        String name = new String(pckgname);
        if (!name.startsWith(S_SLASH)) {
            name = S_SLASH + name;
        }
        name = name.replace('.', '/');
        URL url = RTSI.class.getResource(name);
        System.out.println(name + "->" + url);
        if (url == null) return;
        File directory = new File(url.getFile());
        if (directory.exists()) {
            String[] files = directory.list();
            for (int i = 0; i < files.length; i++) {
                if (files[i].endsWith(".class")) {
                    String classname = files[i].substring(0, files[i].length() - 6);
                    try {
                        Object o = Class.forName(pckgname + S_PERIOD + classname).newInstance();
                        if (tosubclass.isInstance(o)) {
                            System.out.println(classname);
                        }
                    } catch (ClassNotFoundException cnfex) {
                        System.err.println(cnfex);
                    } catch (InstantiationException iex) {
                    } catch (IllegalAccessException iaex) {
                    }
                }
            }
        } else {
            try {
                JarURLConnection conn = (JarURLConnection) url.openConnection();
                String starts = conn.getEntryName();
                JarFile jfile = conn.getJarFile();
                Enumeration<JarEntry> e = jfile.entries();
                while (e.hasMoreElements()) {
                    ZipEntry entry = e.nextElement();
                    String entryname = entry.getName();
                    if (entryname.startsWith(starts) && (entryname.lastIndexOf('/') <= starts.length()) && entryname.endsWith(".class")) {
                        String classname = entryname.substring(0, entryname.length() - 6);
                        if (classname.startsWith(S_SLASH)) classname = classname.substring(1);
                        classname = classname.replace('/', '.');
                        try {
                            Object o = Class.forName(classname).newInstance();
                            if (tosubclass.isInstance(o)) {
                                System.out.println(classname.substring(classname.lastIndexOf('.') + 1));
                            }
                        } catch (ClassNotFoundException cnfex) {
                            System.err.println(cnfex);
                        } catch (InstantiationException iex) {
                        } catch (IllegalAccessException iaex) {
                        }
                    }
                }
            } catch (IOException ioex) {
                System.err.println(ioex);
            }
        }
    }
