    private static void _searchResource(Set<URL> result, ClassLoader loader, String resource, String prefix, String suffix) throws IOException {
        for (Enumeration<URL> urls = loader.getResources(resource); urls.hasMoreElements(); ) {
            URL url = urls.nextElement();
            URLConnection conn = url.openConnection();
            conn.setUseCaches(false);
            conn.setDefaultUseCaches(false);
            JarFile jar;
            if (conn instanceof JarURLConnection) {
                jar = ((JarURLConnection) conn).getJarFile();
            } else {
                jar = _getAlternativeJarFile(url);
            }
            if (jar != null) {
                _searchJar(loader, result, jar, prefix, suffix);
            } else {
                if (!_searchDir(result, new File(URLDecoder.decode(url.getFile(), "UTF-8")), suffix)) {
                    _searchFromURL(result, prefix, suffix, url);
                }
            }
        }
    }
