    public static List<String> getResourceListing(Class<?> cl, String path, boolean includeDirectories, boolean stripPath) {
        try {
            URL dirURL = cl.getClassLoader().getResource(path);
            if (dirURL != null && dirURL.getProtocol().equals("file")) {
                return Arrays.asList(new File(dirURL.toURI()).list());
            }
            if (dirURL == null) {
                String me = cl.getName().replace(".", "/") + ".class";
                dirURL = cl.getClassLoader().getResource(me);
            }
            if (dirURL.getProtocol().equals("jar")) {
                URL urlJar = new URL("jar:" + dirURL.getPath().substring(0, dirURL.getPath().indexOf("!") + 2));
                JarURLConnection conn = (JarURLConnection) urlJar.openConnection();
                JarFile jarFile = conn.getJarFile();
                Enumeration<JarEntry> entries = jarFile.entries();
                Set<String> results = new HashSet<String>();
                while (entries.hasMoreElements()) {
                    String entry = entries.nextElement().getName();
                    if (entry.startsWith(path)) {
                        if (stripPath) entry = entry.substring(path.length() + 1);
                        if (entry.endsWith("/")) {
                            if (includeDirectories) {
                                entry = entry.substring(0, entry.length() - 1);
                                results.add(entry);
                            }
                        } else results.add(entry);
                    }
                }
                return new ArrayList<String>(results);
            }
        } catch (URISyntaxException e) {
        } catch (IOException e) {
        }
        return Collections.emptyList();
    }
