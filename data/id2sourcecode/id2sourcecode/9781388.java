        public void addAllDir(File file) {
            try {
                String s = file.getCanonicalPath().substring(baseDirLength + 1).replace(File.separatorChar, '/') + "/";
                ZipEntry zipentry = new ZipEntry(s);
                zipentry.setTime(file.lastModified());
                try {
                    out.putNextEntry(zipentry);
                } catch (Exception exception) {
                    JOptionPane.showConfirmDialog(null, "err in writing" + zipentry + exception);
                }
            } catch (Exception _ex) {
                System.out.println("Error in adding directory " + file);
            }
            File afile[] = file.listFiles();
            for (int i = 0; i < afile.length; i++) {
                if (afile[i].isDirectory()) {
                    addAllDir(afile[i]);
                } else {
                    addOneFile(afile[i]);
                }
            }
        }
