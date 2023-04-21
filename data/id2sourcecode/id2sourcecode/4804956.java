    private void scanURL(String packagePath, Collection<Object> resources, URL url, Set<String> suffix, FetchType fetchType) throws IOException {
        URLConnection connection = url.openConnection();
        JarFile jarFile;
        if (connection instanceof JarURLConnection) {
            jarFile = ((JarURLConnection) connection).getJarFile();
        } else {
            jarFile = getAlternativeJarFile(url);
        }
        if (jarFile != null) {
            scanJarFile(packagePath, resources, jarFile, suffix, fetchType);
        } else {
            String packageName = packagePath.replace("/", ".");
            if (packageName.endsWith(".")) {
                packageName = packageName.substring(0, packageName.length() - 1);
            }
            scanDir(packageName, new File(url.getFile()), resources, suffix, fetchType);
        }
    }
