    public static void main(String[] argv) {
        ByteArrayOutputStream bos;
        de.fhg.igd.util.URL argUrl;
        StringTokenizer st;
        InputStream in;
        ClassSource cs;
        ArgsParser p;
        ArrayList ucu;
        ArrayList al;
        SortedSet sortedSet;
        Iterator it;
        Resource userClassResource;
        boolean resourceFlag;
        String urlStr;
        String sourceStr;
        String name;
        String ucp;
        String str;
        URL[] userClassUrls;
        URL url;
        int source;
        int i;
        try {
            cs = new ClassSource();
            p = new ArgsParser(options_);
            p.parse(argv);
            if (p.isDefined("help")) {
                printHelp();
                return;
            }
            if (p.isDefined("resource")) {
                resourceFlag = true;
            } else {
                resourceFlag = false;
            }
            if (p.isDefined("userclasspath")) {
                cs.setUserClassPath(p.stringValue("userclasspath"));
            }
            userClassResource = null;
            if (p.isDefined("userclassresource")) {
                try {
                    userClassResource = new MemoryResource();
                    Resources.unzip(new FileInputStream(p.stringValue("userclassresource")), userClassResource);
                    cs.setUserClassResource(userClassResource);
                } catch (Throwable t) {
                    userClassResource = null;
                }
            }
            if (p.isDefined("userclassurls")) {
                ucu = (ArrayList) p.value("userclassurls");
                al = new ArrayList();
                for (i = 0; i < ucu.size(); i++) {
                    try {
                        argUrl = (de.fhg.igd.util.URL) ucu.get(i);
                        if (argUrl.getProtocol().equalsIgnoreCase(FILE_URL_PROTOCOL)) {
                            url = new URL(FILE_URL_PROTOCOL, null, argUrl.getPath());
                        } else {
                            url = new URL(argUrl.toString());
                        }
                        al.add(url);
                    } catch (Throwable t) {
                    }
                }
                userClassUrls = (URL[]) al.toArray(new URL[0]);
            } else {
                userClassUrls = null;
            }
            cs.setUserClassUrls(userClassUrls);
            if (p.isDefined("verbose") || p.isDefined("debug")) {
                System.out.println();
                for (i = 0; i < ClassSource.bootClassPaths_.length; i++) {
                    System.out.println("[sun.boot.class.path] " + ClassSource.bootClassPaths_[i]);
                }
                System.out.println();
                for (i = 0; i < ClassSource.systemJarFiles_.length; i++) {
                    System.out.println("[java.ext.dirs] " + ClassSource.systemJarFiles_[i]);
                }
                System.out.println();
                for (i = 0; i < ClassSource.systemClassPaths_.length; i++) {
                    System.out.println("[java.class.path] " + ClassSource.systemClassPaths_[i]);
                }
                System.out.println();
                for (i = 0; cs.userClassPaths_ != null && i < cs.userClassPaths_.length; i++) {
                    System.out.println("[user.class.path] " + cs.userClassPaths_[i]);
                }
                System.out.println();
                if (userClassResource != null) {
                    System.out.println("[user.class.resource] " + p.stringValue("userclassresource"));
                }
                System.out.println();
                for (i = 0; cs.userClassUrls_ != null && i < cs.userClassUrls_.length; i++) {
                    System.out.println("[user.class.url] " + cs.userClassUrls_[i]);
                }
                System.out.println();
            }
            if (p.isDefined("debug")) {
                System.out.println();
                if (resourceFlag) {
                    System.out.println("List of all system resources:");
                    sortedSet = cs.getSystemResourceNames(SOURCE_ALL);
                } else {
                    System.out.println("List of all system classes:");
                    sortedSet = cs.getSystemClassNames(SOURCE_ALL);
                }
                for (it = sortedSet.iterator(); it.hasNext(); ) {
                    str = (String) it.next();
                    System.out.println("> " + str);
                }
                System.out.println();
                if (resourceFlag) {
                    System.out.println("List of all user resources:");
                    sortedSet = cs.getUserResourceNames(SOURCE_ALL);
                } else {
                    System.out.println("List of all user classes:");
                    sortedSet = cs.getUserClassNames(SOURCE_ALL);
                }
                for (it = sortedSet.iterator(); it.hasNext(); ) {
                    str = (String) it.next();
                    System.out.println("> " + str);
                }
                System.out.println();
            }
            if (p.isDefined("search")) {
                name = p.stringValue("search");
                if (resourceFlag) {
                    System.out.println("ResourceName   = " + name);
                    url = ClassSource.systemResourceUrl(name);
                    if (url == null) {
                        urlStr = cs.userResourceUrl(name);
                        System.out.println("ResourceUrl    = " + urlStr);
                    } else {
                        System.out.println("ResourceUrl    = " + url);
                    }
                    source = ClassSource.systemResourceSource(name);
                    source |= cs.userResourceSource(name);
                    System.out.print("ResourceSource = ");
                } else {
                    System.out.println("ClassName   = " + name);
                    url = ClassSource.systemClassUrl(name);
                    if (url == null) {
                        urlStr = cs.userClassUrl(name);
                        System.out.println("ClassUrl    = " + urlStr);
                    } else {
                        System.out.println("ClassUrl    = " + url);
                    }
                    source = ClassSource.systemClassSource(name);
                    source |= cs.userClassSource(name);
                    System.out.print("ClassSource = ");
                }
                sourceStr = "";
                if ((source & SOURCE_BOOT_CLASSPATH) == SOURCE_BOOT_CLASSPATH) {
                    sourceStr += " | <sun.boot.class.path>";
                }
                if ((source & SOURCE_SYSTEM_JARFILE) == SOURCE_SYSTEM_JARFILE) {
                    sourceStr += " | <java.ext.dirs>";
                }
                if ((source & SOURCE_SYSTEM_CLASSPATH) == SOURCE_SYSTEM_CLASSPATH) {
                    sourceStr += " | <java.class.path>";
                }
                if ((source & SOURCE_USER_LOCALCLASS) == SOURCE_USER_LOCALCLASS) {
                    sourceStr += " | <user.local.class>";
                }
                if ((source & SOURCE_USER_CLASSRESOURCE) == SOURCE_USER_CLASSRESOURCE) {
                    sourceStr += " | <user.class.resource>";
                }
                if ((source & SOURCE_USER_REMOTECLASS) == SOURCE_USER_REMOTECLASS) {
                    sourceStr += " | <user.remote.class>";
                }
                sourceStr = sourceStr.trim();
                if (sourceStr.length() != 0) {
                    sourceStr = sourceStr.substring(2);
                } else {
                    sourceStr = "<not_found>";
                }
                System.out.println(sourceStr);
                if (p.isDefined("hex")) {
                    if (source != SOURCE_NOT_FOUND) {
                        if (resourceFlag) {
                            System.out.println();
                            System.out.println("ResourceHexDump:\n");
                            in = ClassSource.systemResourceInputStream(name);
                            if (in == null) {
                                in = cs.userResourceInputStream(name);
                            }
                        } else {
                            System.out.println();
                            System.out.println("ClassHexDump:\n");
                            in = ClassSource.systemClassInputStream(name);
                            if (in == null) {
                                in = cs.userClassInputStream(name);
                            }
                        }
                        bos = new ByteArrayOutputStream();
                        Pipe.pipe(in, bos);
                        try {
                            in.close();
                        } catch (Throwable t) {
                        }
                        System.out.println(hexDump(bos.toByteArray()));
                    } else {
                        System.out.println();
                        System.out.println("HexDump: <none>\n");
                        System.out.println();
                    }
                }
            }
        } catch (Throwable t) {
            printHelp();
            t.printStackTrace();
        }
    }
