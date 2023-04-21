    public static boolean decompileClass(String className, ClassPath classPath, String classPathStr, ZipOutputStream destZip, String destDir, TabbedPrintWriter writer, ImportHandler imports) {
        try {
            ClassInfo clazz;
            try {
                clazz = classPath.getClassInfo(className);
            } catch (IllegalArgumentException ex) {
                GlobalOptions.err.println("`" + className + "' is not a class name");
                return false;
            }
            if (skipClass(clazz)) return true;
            String filename = className.replace('.', File.separatorChar) + ".java";
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
            GlobalOptions.err.println(className);
            ClassAnalyzer clazzAna = new ClassAnalyzer(clazz, imports);
            clazzAna.dumpJavaFile(writer);
            if (destZip != null) {
                writer.flush();
                destZip.closeEntry();
            } else if (destDir != null) writer.close();
            System.gc();
            return true;
        } catch (FileNotFoundException ex) {
            GlobalOptions.err.println("Can't read " + ex.getMessage() + ".");
            GlobalOptions.err.println("Check the class path (" + classPathStr + ") and check that you use the java class name.");
            return false;
        } catch (ClassFormatException ex) {
            GlobalOptions.err.println("Error while reading " + className + ".");
            ex.printStackTrace(GlobalOptions.err);
            return false;
        } catch (IOException ex) {
            GlobalOptions.err.println("Can't write source of " + className + ".");
            GlobalOptions.err.println("Check the permissions.");
            ex.printStackTrace(GlobalOptions.err);
            return false;
        } catch (RuntimeException ex) {
            GlobalOptions.err.println("Error whilst decompiling " + className + ".");
            ex.printStackTrace(GlobalOptions.err);
            return false;
        } catch (InternalError ex) {
            GlobalOptions.err.println("Internal error whilst decompiling " + className + ".");
            ex.printStackTrace(GlobalOptions.err);
            return false;
        }
    }
