    private List<Class> packageClasses(final ExternalContext externalContext, final String scanPackages) throws ClassNotFoundException, IOException {
        List<Class> list = new ArrayList<Class>();
        String[] scanPackageTokens = scanPackages.split(",");
        for (String scanPackageToken : scanPackageTokens) {
            if (scanPackageToken.toLowerCase().endsWith(".jar")) {
                URL jarResource = externalContext.getResource(WEB_LIB_PREFIX + scanPackageToken);
                String jarURLString = "jar:" + jarResource.toString() + "!/";
                URL url = new URL(jarURLString);
                JarFile jarFile = ((JarURLConnection) url.openConnection()).getJarFile();
                list.addAll(archiveClasses(externalContext, jarFile));
            } else {
                _PackageInfo.getInstance().getClasses(list, scanPackageToken);
            }
        }
        return list;
    }
