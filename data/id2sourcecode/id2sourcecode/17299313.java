    protected void addRepositoryInternal(String repository) {
        URLStreamHandler streamHandler = null;
        String protocol = parseProtocol(repository);
        if (factory != null) streamHandler = factory.createURLStreamHandler(protocol);
        if (!repository.endsWith(File.separator) && !repository.endsWith("/")) {
            JarFile jarFile = null;
            try {
                Manifest manifest = null;
                if (repository.startsWith("jar:")) {
                    URL url = new URL(null, repository, streamHandler);
                    JarURLConnection conn = (JarURLConnection) url.openConnection();
                    conn.setAllowUserInteraction(false);
                    conn.setDoInput(true);
                    conn.setDoOutput(false);
                    conn.connect();
                    jarFile = conn.getJarFile();
                } else if (repository.startsWith("file://")) {
                    jarFile = new JarFile(repository.substring(7));
                } else if (repository.startsWith("file:")) {
                    jarFile = new JarFile(repository.substring(5));
                } else if (repository.endsWith(".jar")) {
                    URL url = new URL(null, repository, streamHandler);
                    URLConnection conn = url.openConnection();
                    JarInputStream jis = new JarInputStream(conn.getInputStream());
                    manifest = jis.getManifest();
                } else {
                    throw new IllegalArgumentException("addRepositoryInternal:  Invalid URL '" + repository + "'");
                }
                if (!((manifest == null) && (jarFile == null))) {
                    if ((manifest == null) && (jarFile != null)) manifest = jarFile.getManifest();
                    if (manifest != null) {
                        Iterator extensions = Extension.getAvailable(manifest).iterator();
                        while (extensions.hasNext()) available.add(extensions.next());
                        extensions = Extension.getRequired(manifest).iterator();
                        while (extensions.hasNext()) required.add(extensions.next());
                    }
                }
            } catch (Throwable t) {
                t.printStackTrace();
                throw new IllegalArgumentException("addRepositoryInternal: " + t);
            } finally {
                if (jarFile != null) {
                    try {
                        jarFile.close();
                    } catch (Throwable t) {
                    }
                }
            }
        }
        synchronized (repositories) {
            String results[] = new String[repositories.length + 1];
            System.arraycopy(repositories, 0, results, 0, repositories.length);
            results[repositories.length] = repository;
            repositories = results;
        }
    }
