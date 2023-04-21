    public static Collection<URL> searchResourcesInClasspath(ClassLoader classLoader, String prefix, String suffix) throws IOException {
        Enumeration<URL>[] enumerations = new Enumeration[] { classLoader.getResources(prefix), classLoader.getResources(prefix + "MANIFEST.MF") };
        Set<URL> urls = new HashSet<URL>();
        for (Enumeration<URL> enumeration : enumerations) {
            while (enumeration.hasMoreElements()) {
                URL url = enumeration.nextElement();
                URLConnection urlConnection = url.openConnection();
                urlConnection.setUseCaches(false);
                urlConnection.setDefaultUseCaches(false);
                if (urlConnection instanceof JarURLConnection) {
                    JarFile jarFile = ((JarURLConnection) urlConnection).getJarFile();
                    if (jarFile != null) {
                        searchJar(classLoader, urls, jarFile, prefix, suffix);
                    } else {
                        searchDir(urls, new File(URLDecoder.decode(url.getFile(), "UTF-8")), suffix);
                    }
                } else if (urlConnection instanceof FileURLConnection) {
                    urls.add(url);
                } else {
                    throw new CommonsRuntimeException("Cannot handler URLConnection of type: " + urlConnection);
                }
            }
        }
        return urls;
    }
