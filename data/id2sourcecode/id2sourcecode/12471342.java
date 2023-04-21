    protected final boolean internalOutputModifiedClass(Object cf, Object mods) throws IOException {
        makeOutputJar();
        String name = toEntryName(getClassName(cf));
        if (entryNames.contains(name)) {
            return false;
        } else {
            putNextEntry(new ZipEntry(name));
            BufferedOutputStream s = new BufferedOutputStream(outputJar);
            writeClassTo(cf, mods, s);
            s.flush();
            outputJar.closeEntry();
            return true;
        }
    }
