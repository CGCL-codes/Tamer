    public StdKunststoffPackager(String outputFilename, PackagerListener plistener) throws Exception {
        super(outputFilename, plistener);
        sendMsg("Copying the Kunststoff library ...");
        InputStream istream = getClass().getResourceAsStream("/lib/kunststoff.jar");
        ZipInputStream skeleton_is;
        if (istream != null) {
            skeleton_is = new ZipInputStream(istream);
        } else {
            skeleton_is = new JarInputStream(new FileInputStream(Compiler.IZPACK_HOME + "lib" + File.separator + "kunststoff.jar"));
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
