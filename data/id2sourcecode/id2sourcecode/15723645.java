    private final Set findSubclasses(final URL location, final String packageName, final Class superClass) {
        synchronized (results) {
            Map thisResult = new TreeMap(CLASS_COMPARATOR);
            Set v = new TreeSet(CLASS_COMPARATOR);
            String fqcn = searchClass.getName();
            List knownLocations = new ArrayList();
            knownLocations.add(location);
            for (int loc = 0; loc < knownLocations.size(); loc++) {
                URL url = (URL) knownLocations.get(loc);
                File directory = new File(url.getFile());
                if (directory.exists()) {
                    String[] files = directory.list();
                    for (int i = 0; i < files.length; i++) {
                        if (files[i].endsWith(".class")) {
                            String classname = files[i].substring(0, files[i].length() - 6);
                            String cls = packageName + "." + classname;
                            if (hasNoLocation(cls)) {
                                continue;
                            }
                            try {
                                Class c = Class.forName(cls);
                                if (superClass.isAssignableFrom(c) && !fqcn.equals(cls)) {
                                    thisResult.put(c, url);
                                }
                            } catch (ClassNotFoundException cnfex) {
                                errors.add(cnfex);
                                negativeResults.add(cls);
                            } catch (Throwable ex) {
                                errors.add(ex);
                                negativeResults.add(cls);
                            }
                        }
                    }
                } else {
                    try {
                        JarURLConnection conn = (JarURLConnection) url.openConnection();
                        JarFile jarFile = conn.getJarFile();
                        Enumeration e = jarFile.entries();
                        while (e.hasMoreElements()) {
                            JarEntry entry = (JarEntry) e.nextElement();
                            String entryname = entry.getName();
                            if (!entry.isDirectory() && entryname.endsWith(".class")) {
                                String classname = entryname.substring(0, entryname.length() - 6);
                                if (classname.startsWith("/")) {
                                    classname = classname.substring(1);
                                }
                                classname = classname.replace('/', '.');
                                if (hasNoLocation(classname)) {
                                    continue;
                                }
                                try {
                                    Class c = Class.forName(classname);
                                    if (superClass.isAssignableFrom(c) && !fqcn.equals(classname)) {
                                        thisResult.put(c, url);
                                    }
                                } catch (ClassNotFoundException cnfex) {
                                    errors.add(cnfex);
                                    negativeResults.add(classname);
                                } catch (NoClassDefFoundError ncdfe) {
                                    errors.add(ncdfe);
                                    negativeResults.add(classname);
                                } catch (UnsatisfiedLinkError ule) {
                                    errors.add(ule);
                                    negativeResults.add(classname);
                                } catch (Exception exception) {
                                    errors.add(exception);
                                    negativeResults.add(classname);
                                } catch (Error error) {
                                    errors.add(error);
                                    negativeResults.add(classname);
                                }
                            }
                        }
                    } catch (IOException ioex) {
                        errors.add(ioex);
                    }
                }
            }
            results.putAll(thisResult);
            Iterator it = thisResult.keySet().iterator();
            while (it.hasNext()) {
                v.add(it.next());
            }
            return v;
        }
    }
