    private String[] getDiagrams() throws ClassNotFoundException {
        logger.debug("Available diagrams are found and listed!");
        ArrayList<String> classesAsString = new ArrayList<String>();
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            if (loader == null) {
                logger.debug("loader is null");
                throw new ClassNotFoundException();
            }
            String path = packageName.replace('.', '/');
            URL url = loader.getResource(path);
            if (url == null) {
                logger.debug("url is null");
                throw new ClassNotFoundException();
            }
            if (url.getProtocol().equals("jar")) {
                URLConnection con = url.openConnection();
                JarURLConnection jarCon = (JarURLConnection) con;
                JarFile jarFile = jarCon.getJarFile();
                JarEntry jarEntry = jarCon.getJarEntry();
                String rootEntryPath = (jarEntry != null ? jarEntry.getName() : "");
                rootEntryPath = rootEntryPath + "/";
                for (Enumeration<JarEntry> entries = jarFile.entries(); entries.hasMoreElements(); ) {
                    JarEntry entry = entries.nextElement();
                    String entryPath = entry.getName();
                    if (entryPath.startsWith(rootEntryPath)) {
                        if (entryPath.endsWith(".class")) {
                            String temp = entryPath.substring(rootEntryPath.length());
                            temp = temp.replace(".class", "");
                            if (!temp.contains("$")) {
                                classesAsString.add(temp);
                            }
                        }
                    }
                }
            } else {
                String rootEntryPath = url.getFile();
                rootEntryPath = rootEntryPath.replace("%20", " ");
                File dir = new File(rootEntryPath);
                File[] dirContents = dir.listFiles();
                for (int i = 0; i < dirContents.length; i++) {
                    File content = dirContents[i];
                    if (content.getName().endsWith(".class")) {
                        if (!content.getAbsolutePath().contains("$")) {
                            String relativePath = content.getAbsolutePath().substring(rootEntryPath.length());
                            String tmpString = "";
                            for (int k = 0; k < relativePath.length(); k++) {
                                if (relativePath.charAt(k) != '/') {
                                    tmpString += relativePath.charAt(k);
                                }
                            }
                            relativePath = tmpString;
                            classesAsString.add(relativePath.substring(0, relativePath.length() - 6));
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new ClassNotFoundException();
        }
        String[] diagrams = new String[classesAsString.size()];
        classesAsString.toArray(diagrams);
        return diagrams;
    }
