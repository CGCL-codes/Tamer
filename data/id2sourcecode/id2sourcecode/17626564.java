    private void searchForClassesImplementingInterface(String pPackageName, HashSet pPackagesAlreadySearched, Class pInterface, HashSet pClassesImplementingInterface) throws IOException, IllegalArgumentException {
        String resourceName = pPackageName.replace('.', '/');
        if (!resourceName.startsWith("/")) {
            resourceName = "/" + resourceName;
        }
        if (resourceName.endsWith("/")) {
            resourceName = resourceName.substring(0, resourceName.length() - 1);
        }
        assert (!resourceName.endsWith("/")) : "resource name ended with slash: " + resourceName;
        URL url = ClassRegistry.class.getResource(resourceName);
        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
        if (null == url) {
            url = pInterface.getResource(resourceName);
            if (null == url) {
                url = systemClassLoader.getResource(resourceName);
            }
        }
        if (null != url) {
            File directory = new File(url.getFile());
            String directoryName = directory.getAbsolutePath();
            if (directory.exists()) {
                String[] files = directory.list();
                int numFiles = files.length;
                for (int fileCtr = 0; fileCtr < numFiles; ++fileCtr) {
                    String fileName = files[fileCtr];
                    if (fileName.equals("CVS")) {
                        continue;
                    }
                    String fullFileName = directoryName + "/" + fileName;
                    File subFile = new File(fullFileName);
                    if (subFile.isDirectory()) {
                        String subPackageResourceName = resourceName + "/" + fileName;
                        String subPackageName = (subPackageResourceName.substring(1, subPackageResourceName.length())).replace('/', '.');
                        if (pPackagesAlreadySearched.contains(subPackageName)) {
                            continue;
                        }
                        pPackagesAlreadySearched.add(subPackageName);
                        searchForClassesImplementingInterface(subPackageName, pPackagesAlreadySearched, pInterface, pClassesImplementingInterface);
                    }
                    if (fileName.endsWith(".class")) {
                        String packageName = null;
                        if (resourceName.startsWith("/")) {
                            packageName = resourceName.substring(1, resourceName.length());
                        } else {
                            packageName = resourceName;
                        }
                        packageName = packageName.replace('/', '.');
                        String className = packageName + "." + fileName.substring(0, fileName.length() - 6);
                        if (!pClassesImplementingInterface.contains(className) && classImplementsInterface(className, pInterface)) {
                            pClassesImplementingInterface.add(className);
                        }
                    }
                }
            } else {
                URLConnection uconn = url.openConnection();
                if (uconn instanceof JarURLConnection) {
                    JarURLConnection conn = (JarURLConnection) uconn;
                    String starts = conn.getEntryName();
                    JarFile jfile = conn.getJarFile();
                    Enumeration e = jfile.entries();
                    while (e.hasMoreElements()) {
                        ZipEntry entry = (ZipEntry) e.nextElement();
                        String entryName = entry.getName();
                        if (entryName.endsWith("/")) {
                            if (entryName.equals(MANIFEST_DIR_NAME + "/")) {
                                continue;
                            }
                            String subPackageResourceName = entryName;
                            String subPackageName = entryName.replace('/', '.');
                            if (pPackagesAlreadySearched.contains(subPackageName)) {
                                continue;
                            }
                            pPackagesAlreadySearched.add(subPackageName);
                            searchForClassesImplementingInterface(subPackageName, pPackagesAlreadySearched, pInterface, pClassesImplementingInterface);
                        }
                        if (entryName.startsWith(starts) && (entryName.lastIndexOf('/') <= starts.length()) && entryName.endsWith(".class")) {
                            String classname = entryName.substring(0, entryName.length() - 6);
                            if (classname.startsWith("/")) classname = classname.substring(1);
                            classname = classname.replace('/', '.');
                            if (!pClassesImplementingInterface.contains(classname) && classImplementsInterface(classname, pInterface)) {
                                pClassesImplementingInterface.add(classname);
                            }
                        }
                    }
                }
            }
        }
    }
