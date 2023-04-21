    public String getClassAnnotation(Class cl) {
        ClassLoader loader = cl.getClassLoader();
        ClassLoader systemLoader = ClassLoader.getSystemClassLoader();
        if (loader == systemLoader || (systemLoader != null && loader == systemLoader.getParent())) {
            return userCodeBase;
        }
        if (loader instanceof URLLoader) {
            return ((URLLoader) loader).getAnnotations();
        } else if (loader instanceof URLClassLoader) {
            URL[] urls = ((URLClassLoader) loader).getURLs();
            String annot = urlsToCodebase(urls);
            if (annot == null) {
                return userCodeBase;
            }
            SecurityManager mgr = System.getSecurityManager();
            if (mgr != null) {
                try {
                    for (int i = 0; i < urls.length; ++i) {
                        Permission p = urls[i].openConnection().getPermission();
                        if (p != null) {
                            mgr.checkPermission(p);
                        }
                    }
                } catch (SecurityException se) {
                    return userCodeBase;
                } catch (IOException ioe) {
                    return userCodeBase;
                }
            }
            return annot;
        } else {
            return userCodeBase;
        }
    }
