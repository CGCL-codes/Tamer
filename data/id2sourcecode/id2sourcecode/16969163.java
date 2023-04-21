    private List<Class> packageClasses(ServletContext servletContext, String scanPackages) throws ClassNotFoundException, IOException {
        List<Class> list = new ArrayList<Class>();
        String[] scanPackageTokens = scanPackages.split(",");
        for (String scanPackageToken : scanPackageTokens) {
            if (scanPackageToken.toLowerCase().endsWith(".jar")) {
                URL jarResource = servletContext.getResource(WEB_LIB_PREFIX + scanPackageToken);
                String jarURLString = "jar:" + jarResource.toString() + "!/";
                URL url = new URL(jarURLString);
                JarFile jarFile = ((JarURLConnection) url.openConnection()).getJarFile();
                list.addAll(archiveClasses(servletContext, jarFile));
            } else {
                PackageInfo.getInstance().getClasses(list, scanPackageToken);
            }
        }
        return list;
    }
