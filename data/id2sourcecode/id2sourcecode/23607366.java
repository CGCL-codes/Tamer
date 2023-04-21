    public static void createJar(OutputStream os, List<String> funcs, PigContext pigContext) throws ClassNotFoundException, IOException {
        Vector<JarListEntry> jarList = new Vector<JarListEntry>();
        for (String toSend : pigPackagesToSend) {
            addContainingJar(jarList, PigMapReduce.class, toSend, pigContext);
        }
        ClassLoader pigClassLoader = PigMapReduce.class.getClassLoader();
        for (String func : funcs) {
            Class clazz = pigContext.getClassForAlias(func);
            if (clazz != null) {
                if (pigClassLoader == clazz.getClassLoader()) {
                    continue;
                }
                addContainingJar(jarList, clazz, null, pigContext);
            }
        }
        HashMap<String, String> contents = new HashMap<String, String>();
        JarOutputStream jarFile = new JarOutputStream(os);
        Iterator<JarListEntry> it = jarList.iterator();
        while (it.hasNext()) {
            JarListEntry jarEntry = it.next();
            mergeJar(jarFile, jarEntry.jar, jarEntry.prefix, contents);
        }
        for (int i = 0; i < pigContext.extraJars.size(); i++) {
            mergeJar(jarFile, pigContext.extraJars.get(i), null, contents);
        }
        if (pigContext != null) {
            jarFile.putNextEntry(new ZipEntry("pigContext"));
            new ObjectOutputStream(jarFile).writeObject(pigContext);
        }
        jarFile.close();
    }
