    public static String[] getURLListFromResource(String resourceName, String regExFilter, boolean onlyFromFirstMatched) throws IOException {
        String[] urlArray;
        Vector urlVector = new Vector();
        Enumeration e;
        ClassLoader classLoader = TestJAR.class.getClassLoader();
        URLClassLoader urlClassLoader = (URLClassLoader) classLoader;
        e = urlClassLoader.findResources(resourceName);
        for (; e.hasMoreElements(); ) {
            URL url = (URL) e.nextElement();
            if ("file".equals(url.getProtocol())) {
                File file = new File(url.getPath());
                File[] fileList = file.listFiles();
                if (fileList != null) {
                    for (int i = 0; i < fileList.length; i++) {
                        String urlStr = fileList[i].toURL().toString();
                        if (urlStr.matches(".*[.mqat]")) {
                            urlVector.add(urlStr);
                        }
                    }
                }
            } else if ("jar".equals(url.getProtocol())) {
                JarURLConnection jarConnection = (JarURLConnection) url.openConnection();
                JarFile jarFile = jarConnection.getJarFile();
                e = jarFile.entries();
                for (; e.hasMoreElements(); ) {
                    JarEntry jarEntry = (JarEntry) e.nextElement();
                    if (!jarEntry.isDirectory()) {
                        String urlStr = url.toString().substring(0, url.toString().lastIndexOf('!') + 1);
                        urlStr += "/" + jarEntry;
                        if (urlStr.matches(".*[.mqat]")) {
                            urlVector.add(urlStr);
                        }
                    }
                }
            }
            if (onlyFromFirstMatched) {
                break;
            }
        }
        urlArray = (String[]) urlVector.toArray(new String[urlVector.size()]);
        return urlArray;
    }
