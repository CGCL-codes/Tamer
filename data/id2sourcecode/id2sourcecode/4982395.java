    public String[] getDirectoryContents(URL url) throws IOException {
        JarURLConnection jcon = (JarURLConnection) url.openConnection();
        String jarDir = jcon.getJarEntry().getName();
        JarFile jarFile = jcon.getJarFile();
        Enumeration entries = jarFile.entries();
        List<String> entryStrings = new ArrayList<String>();
        while (entries.hasMoreElements()) {
            JarEntry entry = (JarEntry) entries.nextElement();
            String name = entry.getName();
            if (name.startsWith(jarDir) && name.length() > jarDir.length()) {
                entryStrings.add(name.replaceFirst(jarDir, ""));
            }
        }
        String[] retValue = new String[entryStrings.size()];
        for (int n = 0; n < retValue.length; n++) {
            retValue[n] = (String) (entryStrings.get(n));
        }
        return retValue;
    }
