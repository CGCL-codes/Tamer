    public static void main(String[] params) {
        int i;
        String classPath = System.getProperty("java.class.path").replace(File.pathSeparatorChar, SearchPath.pathSeparatorChar);
        File destDir = null;
        ZipOutputStream destZip = null;
        int importPackageLimit = ImportHandler.DEFAULT_PACKAGE_LIMIT;
        int importClassLimit = ImportHandler.DEFAULT_CLASS_LIMIT;
        ;
        GlobalOptions.err.println(GlobalOptions.copyright);
        for (i = 0; i < params.length && params[i].startsWith("-"); i++) {
            if (params[i].equals("-v")) GlobalOptions.verboseLevel++; else if (params[i].equals("--dest")) destDir = new File(params[++i]); else if (params[i].startsWith("--debug")) {
                String flags;
                if (params[i].startsWith("--debug=")) {
                    flags = params[i].substring(8);
                } else if (params[i].length() != 7) {
                    usage();
                    return;
                } else {
                    flags = params[++i];
                }
                GlobalOptions.setDebugging(flags);
            } else if (params[i].equals("--style")) {
                String style = params[++i];
                if (style.equals("sun")) outputStyle = SUN_STYLE; else if (style.equals("gnu")) outputStyle = GNU_STYLE; else {
                    GlobalOptions.err.println("Unknown style: " + style);
                    usage();
                    return;
                }
            } else if (params[i].equals("--import")) {
                importPackageLimit = Integer.parseInt(params[++i]);
                importClassLimit = Integer.parseInt(params[++i]);
            } else if (params[i].equals("--classpath")) {
                classPath = params[++i];
            } else if (params[i].equals("--cp")) {
                classPath = params[++i];
            } else if (params[i].equals("--")) {
                i++;
                break;
            } else {
                if (params[i].startsWith("--")) {
                    boolean negated = false;
                    String optionName = params[i].substring(2);
                    if (optionName.startsWith("no")) {
                        optionName = optionName.substring(2);
                        negated = true;
                    }
                    int index = -1;
                    for (int j = 0; j < optionNames.length; j++) {
                        if (optionNames[j].startsWith(optionName)) {
                            if (optionNames[j].equals(optionName)) {
                                index = j;
                                break;
                            }
                            if (index == -1) {
                                index = j;
                            } else {
                                index = -2;
                                break;
                            }
                        }
                    }
                    if (index >= 0) {
                        if (negated) options &= ~(1 << index); else options |= 1 << index;
                        continue;
                    }
                }
                if (!params[i].startsWith("-h") && !params[i].equals("--help")) GlobalOptions.err.println("Unknown option: " + params[i]);
                usage();
                return;
            }
        }
        if (i == params.length) {
            usage();
            return;
        }
        ClassInfo.setClassPath(classPath);
        ImportHandler imports = new ImportHandler(importPackageLimit, importClassLimit);
        TabbedPrintWriter writer = null;
        if (destDir == null) writer = new TabbedPrintWriter(System.out, imports); else if (destDir.getName().endsWith(".zip")) {
            try {
                destZip = new ZipOutputStream(new FileOutputStream(destDir));
            } catch (IOException ex) {
                GlobalOptions.err.println("Can't open zip file " + destDir);
                ex.printStackTrace(GlobalOptions.err);
                return;
            }
            writer = new TabbedPrintWriter(new BufferedOutputStream(destZip), imports, false);
        }
        for (; i < params.length; i++) {
            try {
                ClassInfo clazz;
                try {
                    clazz = ClassInfo.forName(params[i]);
                } catch (IllegalArgumentException ex) {
                    GlobalOptions.err.println("`" + params[i] + "' is not a class name");
                    continue;
                }
                if (skipClass(clazz)) continue;
                String filename = params[i].replace('.', File.separatorChar) + ".java";
                if (destZip != null) {
                    writer.flush();
                    destZip.putNextEntry(new ZipEntry(filename));
                } else if (destDir != null) {
                    File file = new File(destDir, filename);
                    File directory = new File(file.getParent());
                    if (!directory.exists() && !directory.mkdirs()) {
                        GlobalOptions.err.println("Could not create directory " + directory.getPath() + ", check permissions.");
                    }
                    writer = new TabbedPrintWriter(new BufferedOutputStream(new FileOutputStream(file)), imports, false);
                }
                GlobalOptions.err.println(params[i]);
                ClassAnalyzer clazzAna = new ClassAnalyzer(clazz, imports);
                clazzAna.dumpJavaFile(writer);
                if (destZip != null) {
                    writer.flush();
                    destZip.closeEntry();
                } else if (destDir != null) writer.close();
                System.gc();
            } catch (IOException ex) {
                GlobalOptions.err.println("Can't write source of " + params[i] + ".");
                GlobalOptions.err.println("Check the permissions.");
                ex.printStackTrace(GlobalOptions.err);
            }
        }
        if (destZip != null) {
            try {
                destZip.close();
            } catch (IOException ex) {
                GlobalOptions.err.println("Can't close Zipfile");
                ex.printStackTrace(GlobalOptions.err);
            }
        }
    }
