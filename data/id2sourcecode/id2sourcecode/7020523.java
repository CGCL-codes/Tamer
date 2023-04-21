    public static List findLibJars(URL url) {
        ArrayList embedded_jars = new ArrayList();
        try {
            ZipInputStream zin = new ZipInputStream(url.openStream());
            ZipEntry entry;
            String entryName;
            while ((entry = zin.getNextEntry()) != null) {
                entryName = entry.getName();
                if (entryName != null && (entryName.startsWith("lib/") || entryName.startsWith("Lib/")) && entryName.endsWith(".jar")) {
                    embedded_jars.add(entryName);
                }
            }
            zin.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return embedded_jars;
    }
