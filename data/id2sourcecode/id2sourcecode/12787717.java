    public static ArrayList<String> GetMetricsAvailable() {
        ArrayList<String> outputVect = new ArrayList<String>();
        Class tosubclass = null;
        try {
            tosubclass = Class.forName("uk.ac.shef.wit.simmetrics.similaritymetrics.AbstractStringMetric");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        String pckgname = "uk.ac.shef.wit.simmetrics.similaritymetrics";
        String name = pckgname;
        if (!name.startsWith("/")) {
            name = "/" + name;
        }
        name = name.replace('.', '/');
        URL url = aMetric.getClass().getResource(name);
        if (url == null) return null;
        File directory = new File(url.getFile());
        if (directory.exists()) {
            String[] files = directory.list();
            for (String file : files) {
                if (file.endsWith(".class")) {
                    String classname = file.substring(0, file.length() - 6);
                    try {
                        Object o = Class.forName(pckgname + "." + classname).newInstance();
                        assert tosubclass != null;
                        if (tosubclass.isInstance(o)) {
                            outputVect.add(classname);
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
                        if (classname.startsWith("/")) classname = classname.substring(1);
                        classname = classname.replace('/', '.');
                        try {
                            Object o = Class.forName(classname).newInstance();
                            assert tosubclass != null;
                            if (tosubclass.isInstance(o)) {
                                outputVect.add(classname.substring(classname.lastIndexOf('.') + 1));
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
        return outputVect;
    }
