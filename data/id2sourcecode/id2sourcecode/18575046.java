    @SuppressWarnings("unchecked")
    private Set<String> loadBeanClasses() {
        Set<String> results = new HashSet<String>();
        try {
            Enumeration<URL> resources = org.springframework.util.ClassUtils.getDefaultClassLoader().getResources(PERSISTENT_BEANS_DEFINITION_LOCATION);
            Module: while (resources.hasMoreElements()) {
                URL url = resources.nextElement();
                InputStream is = null;
                try {
                    URLConnection con = url.openConnection();
                    con.setUseCaches(false);
                    is = con.getInputStream();
                    List<String> lines = IOUtils.readLines(is, "ISO-8859-1");
                    String moduleName = null;
                    if (lines != null) {
                        for (Iterator<String> iterator = lines.iterator(); iterator.hasNext(); ) {
                            String line = iterator.next();
                            if (!line.startsWith("#")) {
                                if (moduleName == null) {
                                    moduleName = line;
                                    if (excludedModuleNames != null && excludedModuleNames.contains(moduleName)) {
                                        continue Module;
                                    }
                                } else {
                                    results.add(line);
                                }
                            }
                        }
                    }
                } finally {
                    if (is != null) {
                        is.close();
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return results;
    }
