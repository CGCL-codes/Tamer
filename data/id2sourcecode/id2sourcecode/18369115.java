    private String getLoaderAnnotation(ClassLoader loader, boolean check) {
        if (isLocalLoader(loader)) {
            return getClassAnnotation(loader);
        }
        String annotation = null;
        if (loader instanceof ClassAnnotation) {
            annotation = ((ClassAnnotation) loader).getClassAnnotation();
        } else if (loader instanceof URLClassLoader) {
            try {
                URL[] urls = ((URLClassLoader) loader).getURLs();
                if (urls != null) {
                    if (check) {
                        SecurityManager sm = System.getSecurityManager();
                        if (sm != null) {
                            Permissions perms = new Permissions();
                            for (int i = 0; i < urls.length; i++) {
                                Permission p = urls[i].openConnection().getPermission();
                                if (p != null) {
                                    if (!perms.implies(p)) {
                                        sm.checkPermission(p);
                                        perms.add(p);
                                    }
                                }
                            }
                        }
                    }
                    annotation = PreferredClassLoader.urlsToPath(urls);
                }
            } catch (SecurityException e) {
            } catch (IOException e) {
            }
        }
        if (annotation != null) {
            return annotation;
        } else {
            return getClassAnnotation(loader);
        }
    }
