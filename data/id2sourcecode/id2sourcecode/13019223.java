        private static Process createProcess(String localHostAddress, int port) {
            List<String> classPathList = new ArrayList<String>();
            String pathSeparator = System.getProperty("path.separator");
            List<Object> referenceList = new ArrayList<Object>();
            List<String> optionalReferenceList = new ArrayList<String>();
            referenceList.add(NativeSwing.class);
            referenceList.add(NativeInterface.class);
            referenceList.add("org/eclipse/swt/widgets/Display.class");
            optionalReferenceList.add("org/mozilla/xpcom/Mozilla.class");
            optionalReferenceList.add("org/mozilla/interfaces/nsIWebBrowser.class");
            for (String optionalReference : optionalReferenceList) {
                if (NativeInterface.class.getClassLoader().getResource(optionalReference) != null) {
                    referenceList.add(optionalReference);
                }
            }
            Class<?>[] nativeClassPathReferenceClasses = nativeInterfaceConfiguration.getNativeClassPathReferenceClasses();
            if (nativeClassPathReferenceClasses != null) {
                referenceList.addAll(Arrays.asList(nativeClassPathReferenceClasses));
            }
            String[] nativeClassPathReferenceResources = nativeInterfaceConfiguration.getNativeClassPathReferenceResources();
            if (nativeClassPathReferenceResources != null) {
                referenceList.addAll(Arrays.asList(nativeClassPathReferenceResources));
            }
            boolean isProxyClassLoaderUsed = Boolean.parseBoolean(System.getProperty("nativeswing.peervm.forceproxyclassloader"));
            if (!isProxyClassLoaderUsed) {
                for (Object o : referenceList) {
                    File clazzClassPath;
                    if (o instanceof Class) {
                        clazzClassPath = Utils.getClassPathFile((Class<?>) o);
                    } else {
                        clazzClassPath = Utils.getClassPathFile((String) o);
                        if (NativeInterface.class.getClassLoader().getResource((String) o) == null) {
                            throw new IllegalStateException("A resource that is needed in the classpath is missing: " + o);
                        }
                    }
                    clazzClassPath = o instanceof Class ? Utils.getClassPathFile((Class<?>) o) : Utils.getClassPathFile((String) o);
                    if (clazzClassPath != null) {
                        String path = clazzClassPath.getAbsolutePath();
                        if (!classPathList.contains(path)) {
                            classPathList.add(path);
                        }
                    } else {
                        isProxyClassLoaderUsed = true;
                    }
                }
            }
            if (isProxyClassLoaderUsed) {
                classPathList.clear();
                File classPathFile = new File(System.getProperty("java.io.tmpdir"), ".djnativeswing/classpath");
                Utils.deleteAll(classPathFile);
                String classPath = NetworkURLClassLoader.class.getName().replace('.', '/') + ".class";
                File mainClassFile = new File(classPathFile, classPath);
                mainClassFile.getParentFile().mkdirs();
                if (!mainClassFile.exists()) {
                    try {
                        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(mainClassFile));
                        BufferedInputStream in = new BufferedInputStream(NativeInterface.class.getResourceAsStream("/" + classPath));
                        byte[] bytes = new byte[1024];
                        for (int n; (n = in.read(bytes)) != -1; out.write(bytes, 0, n)) {
                        }
                        in.close();
                        out.close();
                    } catch (Exception e) {
                    }
                    mainClassFile.deleteOnExit();
                }
                classPathList.add(classPathFile.getAbsolutePath());
            }
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < classPathList.size(); i++) {
                if (i > 0) {
                    sb.append(pathSeparator);
                }
                sb.append(classPathList.get(i));
            }
            String javaHome = System.getProperty("java.home");
            String[] candidateBinaries = new String[] { new File(javaHome, "bin/java").getAbsolutePath(), new File("/usr/lib/java").getAbsolutePath(), "java" };
            Process p = null;
            List<String> argList = new ArrayList<String>();
            argList.add(null);
            String[] peerVMParams = nativeInterfaceConfiguration.getPeerVMParams();
            if (peerVMParams != null) {
                for (String param : peerVMParams) {
                    argList.add(param);
                }
            }
            String[] flags = new String[] { "nativeswing.interface.syncmessages", "nativeswing.interface.debug.printmessages", "nativeswing.peervm.debug.printstartmessage", "nativeswing.swt.debug.device", "nativeswing.swt.devicedata.debug", "nativeswing.swt.devicedata.tracking" };
            for (String flag : flags) {
                if (Boolean.parseBoolean(System.getProperty(flag))) {
                    argList.add("-D" + flag + "=true");
                }
            }
            argList.add("-Dnativeswing.localhostaddress=" + localHostAddress);
            argList.add("-classpath");
            argList.add(sb.toString());
            if (isProxyClassLoaderUsed) {
                argList.add(NetworkURLClassLoader.class.getName());
                argList.add(WebServer.getDefaultWebServer().getClassPathResourceURL("", ""));
            }
            argList.add(NativeInterface.class.getName());
            argList.add(String.valueOf(port));
            String javaVersion = System.getProperty("java.version");
            if (javaVersion != null && javaVersion.compareTo("1.6.0_10") >= 0 && "Sun Microsystems Inc.".equals(System.getProperty("java.vendor"))) {
                boolean isTryingAppletCompatibility = true;
                if (peerVMParams != null) {
                    for (String peerVMParam : peerVMParams) {
                        if (peerVMParam.startsWith("-Xbootclasspath/a:")) {
                            isTryingAppletCompatibility = false;
                            break;
                        }
                    }
                }
                if (isTryingAppletCompatibility) {
                    File[] deploymentFiles = new File[] { new File(javaHome, "lib/deploy.jar"), new File(javaHome, "lib/plugin.jar"), new File(javaHome, "lib/javaws.jar") };
                    List<String> argListX = new ArrayList<String>();
                    argListX.add(candidateBinaries[0]);
                    StringBuilder sbX = new StringBuilder();
                    for (int i = 0; i < deploymentFiles.length; i++) {
                        if (i != 0) {
                            sbX.append(pathSeparator);
                        }
                        File deploymentFile = deploymentFiles[i];
                        if (deploymentFile.exists()) {
                            sbX.append(deploymentFile.getAbsolutePath());
                        }
                    }
                    if (sbX.indexOf(" ") != -1) {
                        argListX.add("\"-Xbootclasspath/a:" + sbX + "\"");
                    } else {
                        argListX.add("-Xbootclasspath/a:" + sbX);
                    }
                    argListX.addAll(argList.subList(1, argList.size()));
                    if (Boolean.parseBoolean(System.getProperty("nativeswing.peervm.debug.printcommandline"))) {
                        System.err.println("Native Command: " + Arrays.toString(argListX.toArray()));
                    }
                    try {
                        p = new ProcessBuilder(argListX).start();
                    } catch (IOException e) {
                    }
                }
            }
            if (p == null) {
                for (String candidateBinary : candidateBinaries) {
                    argList.set(0, candidateBinary);
                    if (Boolean.parseBoolean(System.getProperty("nativeswing.peervm.debug.printcommandline"))) {
                        System.err.println("Native Command: " + Arrays.toString(argList.toArray()));
                    }
                    try {
                        p = new ProcessBuilder(argList).start();
                        break;
                    } catch (IOException e) {
                    }
                }
            }
            if (p == null) {
                throw new IllegalStateException("Failed to spawn the VM!");
            }
            return p;
        }
