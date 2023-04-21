    private static void doListNameOfPackageInJar(final URL baseUrl, final URL urlPath, final List<String> result, final String clazzPkg, final String extenion, final boolean fullPkgName) {
        try {
            final JarURLConnection conn = (JarURLConnection) urlPath.openConnection();
            final JarFile jfile = conn.getJarFile();
            final Enumeration<JarEntry> e = jfile.entries();
            ZipEntry entry;
            String entryname;
            while (e.hasMoreElements()) {
                entry = e.nextElement();
                entryname = entry.getName();
                if (entryname.startsWith(clazzPkg) && entryname.endsWith(extenion)) {
                    if (fullPkgName) result.add(entryname.substring(0, entryname.lastIndexOf('.')).replace('/', '.')); else result.add(entryname.substring(entryname.lastIndexOf('/') + 1, entryname.lastIndexOf('.')));
                }
            }
        } catch (final IOException ioex) {
        }
    }
