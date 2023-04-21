    public WebKunststoffPackager(String outputFilename, PackagerListener plistener) throws Exception {
        super(outputFilename, plistener);
        sendMsg("Copying the Kunststoff library ...");
        ZipInputStream skeleton_is = null;
        InputStream rawInput = getClass().getResourceAsStream("/lib/kunststoff.jar");
        if (rawInput == null) {
            skeleton_is = new JarInputStream(new FileInputStream(Compiler.IZPACK_HOME + "lib" + File.separator + "kunststoff.jar"));
        } else {
            skeleton_is = new ZipInputStream(rawInput);
        }
        ZipEntry zentry;
        while ((zentry = skeleton_is.getNextEntry()) != null) {
            if (zentry.isDirectory()) continue;
            outJar.putNextEntry(new ZipEntry(zentry.getName()));
            copyStream(skeleton_is, outJar);
            outJar.closeEntry();
            skeleton_is.closeEntry();
        }
    }
