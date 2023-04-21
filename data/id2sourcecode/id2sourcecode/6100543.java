    @SuppressWarnings("unchecked")
    private final Vector<Class<?>> findSubclasses(URL location, String packageName, Class<?> superClass) throws Exception {
        synchronized (results) {
            Map<Class<?>, URL> thisResult = new TreeMap<Class<?>, URL>(CLASS_COMPARATOR);
            Vector<Class<?>> v = new Vector<Class<?>>();
            String fqcn = searchClass.getName();
            List<URL> knownLocations = new ArrayList<URL>();
            knownLocations.add(location);
            for (int loc = 0; loc < knownLocations.size(); loc++) {
                URL url = knownLocations.get(loc);
                File directory = new File(url.getFile());
                if (directory.exists()) {
                    String[] files = directory.list();
                    for (int i = 0; i < files.length; i++) {
                        if (files[i].endsWith(".class")) {
                            String classname = files[i].substring(0, files[i].length() - 6);
                            try {
                                Class<?> c = Class.forName(packageName + "." + classname);
                                if (superClass.isAssignableFrom(c) && !fqcn.equals(packageName + "." + classname)) {
                                    thisResult.put(c, url);
                                }
                            } catch (java.lang.ExceptionInInitializerError err) {
                                logger.warn(err);
                            } catch (java.lang.NoClassDefFoundError err) {
                                logger.warn(err);
                            } catch (ClassNotFoundException cnfex) {
                                logger.warn(cnfex);
                            } catch (Exception ex) {
                                logger.warn(ex.getMessage());
                            }
                        }
                    }
                } else {
                    try {
                        JarURLConnection conn = (JarURLConnection) url.openConnection();
                        JarFile jarFile = conn.getJarFile();
                        Enumeration<JarEntry> e = jarFile.entries();
                        while (e.hasMoreElements()) {
                            JarEntry entry = e.nextElement();
                            String entryname = entry.getName();
                            if (!entry.isDirectory() && entryname.endsWith(".class")) {
                                String classname = entryname.substring(0, entryname.length() - 6);
                                if (classname.startsWith("/")) classname = classname.substring(1);
                                classname = classname.replace('/', '.');
                                try {
                                    Class c = Class.forName(classname);
                                    if (superClass.isAssignableFrom(c) && !fqcn.equals(classname)) {
                                        thisResult.put(c, url);
                                    }
                                } catch (java.lang.ExceptionInInitializerError err) {
                                    logger.warn(err);
                                } catch (ClassNotFoundException cnfex) {
                                    logger.warn(cnfex);
                                } catch (NoClassDefFoundError ncdfe) {
                                    logger.warn(ncdfe);
                                } catch (UnsatisfiedLinkError ule) {
                                    logger.warn(ule);
                                } catch (Exception exception) {
                                    logger.warn(exception);
                                } catch (Error error) {
                                    logger.warn(error);
                                }
                            }
                        }
                    } catch (IOException ioex) {
                        logger.error(ioex);
                    }
                }
            }
            results.putAll(thisResult);
            Iterator<Class<?>> it = thisResult.keySet().iterator();
            while (it.hasNext()) {
                v.add(it.next());
            }
            return v;
        }
    }
