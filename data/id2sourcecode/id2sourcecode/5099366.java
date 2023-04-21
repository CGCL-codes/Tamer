    public static File[] getExtras(final ConnectionTypeDescriptor serverType) {
        File mc4jLibDir = new File("mc4jlib");
        File[] commonLibs = mc4jLibDir.listFiles(new FileFilter() {

            public boolean accept(File file) {
                return (!file.isDirectory() && (file.getName().toLowerCase().endsWith(".jar") || file.getName().toLowerCase().endsWith(".zip")));
            }
        });
        File[] tempDirs = mc4jLibDir.listFiles(new FileFilter() {

            public boolean accept(File file) {
                return (file.isDirectory() && file.getName().equals(serverType.getExtrasLibrary()));
            }
        });
        File[] serverLibs = new File[0];
        if (tempDirs.length != 1) {
        } else {
            File serverLibDir = tempDirs[0];
            serverLibs = serverLibDir.listFiles(new FileFilter() {

                public boolean accept(File file) {
                    return (!file.isDirectory() && (file.getName().toLowerCase().endsWith(".jar") || file.getName().toLowerCase().endsWith(".zip")));
                }
            });
        }
        File[] results = new File[commonLibs.length + serverLibs.length + 1];
        System.arraycopy(commonLibs, 0, results, 0, commonLibs.length);
        System.arraycopy(serverLibs, 0, results, commonLibs.length, serverLibs.length);
        results[results.length - 1] = new File("dashboards");
        return results;
    }
