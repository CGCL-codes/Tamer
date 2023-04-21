        public synchronized void addDir(File directory) {
            try {
                String s = directory.getCanonicalPath().substring(baseDirLength + 1).replace(File.separatorChar, '/') + "/";
                ZipEntry zipentry = new ZipEntry(s);
                zipentry.setTime(directory.lastModified());
                try {
                    zipOutStream.putNextEntry(zipentry);
                } catch (Exception exception) {
                }
            } catch (Exception _ex) {
                System.out.println("Error in adding directory " + directory);
            }
            File[] afile = directory.listFiles();
            for (int i = 0; i < afile.length; i++) {
                if (afile[i].isDirectory()) {
                    addDir(afile[i]);
                } else {
                    addFile(afile[i]);
                }
            }
        }
