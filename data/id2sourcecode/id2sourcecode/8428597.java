    private void extractLibraries(JarFile jar) {
        Enumeration<JarEntry> entries = jar.entries();
        byte[] buffer = new byte[8192];
        StringTokenizer token = new StringTokenizer(System.getProperty("java.library.path"), File.pathSeparator);
        String libraryPath = null;
        while (token.hasMoreTokens()) {
            File libDir = new File(token.nextToken());
            if (libDir.canWrite()) {
                try {
                    libraryPath = libDir.getCanonicalPath() + File.separator;
                } catch (IOException e) {
                    logger.info(e.getMessage());
                }
                break;
            }
        }
        if (libraryPath == null) {
            libraryPath = "./";
        }
        String ext;
        if (OSInfo.getLocalOSType().equals(OSInfo.OperationSystemType.Windows)) {
            ext = "dll";
        } else {
            ext = "so";
        }
        while (entries.hasMoreElements()) {
            JarEntry e = entries.nextElement();
            String name = PathUtil.getFileName(e.getName());
            if (name != null && name.endsWith(ext)) {
                FileOutputStream fos = null;
                BufferedInputStream is = null;
                try {
                    is = new BufferedInputStream(jar.getInputStream(e));
                    File libFile = new File(libraryPath + name);
                    libFile.deleteOnExit();
                    if (NodeAgent.ISVERBOSE) {
                        logger.info("Extract lib to: " + libFile.getCanonicalPath());
                    }
                    fos = new FileOutputStream(libFile);
                    int readBytes = 0;
                    while (-1 != (readBytes = is.read(buffer, 0, 8192))) {
                        fos.write(buffer, 0, readBytes);
                    }
                    is.close();
                    fos.close();
                } catch (IOException e1) {
                    logger.info(e1.getMessage());
                } finally {
                    try {
                        is.close();
                        fos.close();
                    } catch (IOException e1) {
                    }
                }
            }
        }
        buffer = null;
    }
