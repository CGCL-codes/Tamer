    static long writeJAR(String midletName, String midletNameShort, File dictDir, JarInputStream in, File jarOutputFile) throws IOException {
        Manifest manifest = new Manifest(in.getManifest());
        Attributes manifestAttributes = manifest.getMainAttributes();
        manifestAttributes.putValue("MIDlet-Name", midletNameShort);
        manifestAttributes.putValue("MIDlet-1", buildMidlet1Name(midletName));
        JarOutputStream out = new JarOutputStream(new FileOutputStream(jarOutputFile), manifest);
        byte[] b = new byte[3000];
        int readBytes;
        JarEntry nextOne;
        while ((nextOne = in.getNextJarEntry()) != null) {
            boolean includeEntry = true;
            String[] excludeEntries = { "de/kugihan/dictionaryformids/dataaccess/zip" };
            for (int i = 0; i < excludeEntries.length; ++i) {
                if (nextOne.getName().startsWith(excludeEntries[i])) {
                    includeEntry = false;
                    break;
                }
            }
            if (isLanguageIconFileNotNeeded(nextOne.getName())) includeEntry = false;
            if (includeEntry) {
                out.putNextEntry(nextOne);
                while ((readBytes = in.read(b, 0, 3000)) != -1) {
                    out.write(b, 0, readBytes);
                }
            }
        }
        in.close();
        addDirectory(out, dictDir, "");
        out.close();
        return jarOutputFile.length();
    }
