        static void deleteObsoleteFiles(File rootDir, File srcDir, Set seenFiles) {
            if (!(rootDir.isDirectory() && srcDir.isDirectory())) throw new IllegalArgumentException();
            String absolutePath = srcDir.getAbsolutePath();
            if (absolutePath.length() <= 5) return;
            if (absolutePath.startsWith("/home/") && (absolutePath.indexOf("/", 6) >= absolutePath.length() - 1 || absolutePath.indexOf("/", 6) < 0)) return;
            File[] files = srcDir.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) deleteObsoleteFiles(rootDir, files[i], seenFiles); else if (seenFiles.contains(files[i])) ; else {
                    deleteXmlBeansFile(files[i]);
                    deleteDirRecursively(rootDir, files[i].getParentFile());
                }
            }
        }
