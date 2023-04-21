    public SortedSet getUserResourceNames0(int resourceSource, boolean onlyClasses) {
        ZipInputStream zipIn;
        SortedSet resources;
        SortedSet set;
        ZipEntry zipEntry;
        Iterator it;
        String name;
        URL url;
        int i;
        resources = new TreeSet();
        zipIn = null;
        if ((resourceSource & SOURCE_USER_LOCALCLASS) == SOURCE_USER_LOCALCLASS) {
            for (i = 0; userClassPaths_ != null && i < userClassPaths_.length; i++) {
                set = getResourceNames(new File(userClassPaths_[i]), onlyClasses);
                resources.addAll(set);
            }
            for (i = 0; userClassUrls_ != null && i < userClassUrls_.length; i++) {
                if (userClassUrls_[i].getProtocol().equalsIgnoreCase(FILE_URL_PROTOCOL)) {
                    set = getResourceNames(new File(userClassUrls_[i].getFile()), onlyClasses);
                    resources.addAll(set);
                }
            }
        }
        if ((resourceSource & SOURCE_USER_CLASSRESOURCE) == SOURCE_USER_CLASSRESOURCE) {
            try {
                for (it = userClassResource_.list().iterator(); it.hasNext(); ) {
                    name = (String) it.next();
                    if (onlyClasses) {
                        if (checkFileExtension(name, CLASS_EXTENSION)) {
                            resources.add(name);
                        }
                    } else {
                        resources.add(name);
                    }
                }
            } catch (Throwable t) {
            }
        }
        if ((resourceSource & SOURCE_USER_REMOTECLASS) == SOURCE_USER_REMOTECLASS) {
            for (i = 0; userClassUrls_ != null && i < userClassUrls_.length; i++) {
                url = userClassUrls_[i];
                if (!url.getProtocol().equalsIgnoreCase(FILE_URL_PROTOCOL) && (checkFileExtension(url.getFile(), JAR_EXTENSION) || checkFileExtension(url.getFile(), ZIP_EXTENSION))) {
                    try {
                        zipIn = new ZipInputStream(url.openStream());
                        while ((zipEntry = zipIn.getNextEntry()) != null) {
                            if (zipEntry.isDirectory()) {
                                continue;
                            }
                            name = zipEntry.getName();
                            if (onlyClasses) {
                                if (checkFileExtension(name, CLASS_EXTENSION)) {
                                    resources.add(name);
                                }
                            } else {
                                resources.add(name);
                            }
                        }
                    } catch (Throwable t) {
                    } finally {
                        try {
                            zipIn.close();
                        } catch (Exception e) {
                        }
                    }
                }
            }
        }
        return resources;
    }
