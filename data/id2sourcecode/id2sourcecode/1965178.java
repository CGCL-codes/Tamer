    public SunGraphicsEnvironment() {
        java.security.AccessController.doPrivileged(new java.security.PrivilegedAction() {

            public Object run() {
                String osName = System.getProperty("os.name");
                if ("Linux".equals(osName)) {
                    isLinux = true;
                } else if ("SunOS".equals(osName)) {
                    isSolaris = true;
                    String version = System.getProperty("os.version", "0.0");
                    try {
                        float ver = Float.parseFloat(version);
                        if (ver > 5.10f) {
                            File f = new File("/etc/release");
                            FileInputStream fis = new FileInputStream(f);
                            InputStreamReader isr = new InputStreamReader(fis, "ISO-8859-1");
                            BufferedReader br = new BufferedReader(isr);
                            String line = br.readLine();
                            if (line.indexOf("OpenSolaris") >= 0) {
                                isOpenSolaris = true;
                            } else {
                                String courierNew = "/usr/openwin/lib/X11/fonts/TrueType/CourierNew.ttf";
                                File courierFile = new File(courierNew);
                                isOpenSolaris = !courierFile.exists();
                            }
                            fis.close();
                        }
                    } catch (Exception e) {
                    }
                }
                noType1Font = "true".equals(System.getProperty("sun.java2d.noType1Font"));
                jreLibDirName = System.getProperty("java.home", "") + File.separator + "lib";
                jreFontDirName = jreLibDirName + File.separator + "fonts";
                if (useAbsoluteFontFileNames()) {
                    lucidaSansFileName = jreFontDirName + File.separator + "LucidaSansRegular.ttf";
                } else {
                    lucidaSansFileName = "LucidaSansRegular.ttf";
                }
                File badFontFile = new File(jreFontDirName + File.separator + "badfonts.txt");
                if (badFontFile.exists()) {
                    FileInputStream fis = null;
                    try {
                        badFonts = new ArrayList();
                        fis = new FileInputStream(badFontFile);
                        InputStreamReader isr = new InputStreamReader(fis);
                        BufferedReader br = new BufferedReader(isr);
                        while (true) {
                            String name = br.readLine();
                            if (name == null) {
                                break;
                            } else {
                                if (debugFonts) {
                                    logger.warning("read bad font: " + name);
                                }
                                badFonts.add(name);
                            }
                        }
                    } catch (IOException e) {
                        try {
                            if (fis != null) {
                                fis.close();
                            }
                        } catch (IOException ioe) {
                        }
                    }
                }
                if (isLinux) {
                    registerFontDir(jreFontDirName);
                }
                registerFontsInDir(jreFontDirName, true, Font2D.JRE_RANK, true, false);
                registerJREFontsWithPlatform(jreFontDirName);
                fontConfig = createFontConfiguration();
                getPlatformFontPathFromFontConfig();
                String extraFontPath = fontConfig.getExtraFontPath();
                boolean prependToPath = false;
                boolean appendToPath = false;
                String dbgFontPath = System.getProperty("sun.java2d.fontpath");
                if (dbgFontPath != null) {
                    if (dbgFontPath.startsWith("prepend:")) {
                        prependToPath = true;
                        dbgFontPath = dbgFontPath.substring("prepend:".length());
                    } else if (dbgFontPath.startsWith("append:")) {
                        appendToPath = true;
                        dbgFontPath = dbgFontPath.substring("append:".length());
                    }
                }
                if (debugFonts) {
                    logger.info("JRE font directory: " + jreFontDirName);
                    logger.info("Extra font path: " + extraFontPath);
                    logger.info("Debug font path: " + dbgFontPath);
                }
                if (dbgFontPath != null) {
                    fontPath = getPlatformFontPath(noType1Font);
                    if (extraFontPath != null) {
                        fontPath = extraFontPath + File.pathSeparator + fontPath;
                    }
                    if (appendToPath) {
                        fontPath = fontPath + File.pathSeparator + dbgFontPath;
                    } else if (prependToPath) {
                        fontPath = dbgFontPath + File.pathSeparator + fontPath;
                    } else {
                        fontPath = dbgFontPath;
                    }
                    registerFontDirs(fontPath);
                } else if (extraFontPath != null) {
                    registerFontDirs(extraFontPath);
                }
                if (isSolaris && Locale.JAPAN.equals(Locale.getDefault())) {
                    registerFontDir("/usr/openwin/lib/locale/ja/X11/fonts/TT");
                }
                initCompositeFonts(fontConfig, null);
                defaultFont = new Font(Font.DIALOG, Font.PLAIN, 12);
                return null;
            }
        });
    }
