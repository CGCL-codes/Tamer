    public static String getClassAnnotation(Class cl) {
        String name = cl.getName();
        int nameLength = name.length();
        if (nameLength > 0 && name.charAt(0) == '[') {
            int i = 1;
            while (nameLength > i && name.charAt(i) == '[') {
                i++;
            }
            if (nameLength > i && name.charAt(i) != 'L') {
                return null;
            }
        }
        ClassLoader loader = cl.getClassLoader();
        if (loader == null || codebaseLoaders.containsKey(loader)) {
            return codebaseProperty;
        }
        String annotation = null;
        if (loader instanceof Loader) {
            annotation = ((Loader) loader).getClassAnnotation();
        } else if (loader instanceof URLClassLoader) {
            try {
                URL[] urls = ((URLClassLoader) loader).getURLs();
                if (urls != null) {
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
                    annotation = urlsToPath(urls);
                }
            } catch (SecurityException e) {
            } catch (IOException e) {
            }
        }
        if (annotation != null) {
            return annotation;
        } else {
            return codebaseProperty;
        }
    }
