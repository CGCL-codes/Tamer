    public static boolean copyDirectory(File directory, String pathExport, boolean copySubDirectory, boolean createdirectory) throws IOException {
        boolean succes = true;
        if (directory.exists() && directory.isDirectory()) {
            if (createdirectory) {
                new File(pathExport + "/" + directory.getName()).mkdirs();
            } else {
                new File(pathExport).mkdirs();
            }
            Collection<File> listeFiles = getListeFile(directory, FILTER_ALLFILE, false, false);
            Iterator<File> it = listeFiles.iterator();
            while (it.hasNext()) {
                File file = (File) it.next();
                if (createdirectory) {
                    copyFile(file, pathExport + "/" + directory.getName(), file.getName());
                } else {
                    copyFile(file, pathExport, file.getName());
                }
            }
            if (copySubDirectory) {
                Collection<File> listeDirectory = getListeDirectory(directory, false);
                it = listeDirectory.iterator();
                while (it.hasNext()) {
                    File subdirectory = (File) it.next();
                    boolean opSucces = false;
                    if (createdirectory) {
                        opSucces = copyDirectory(subdirectory, pathExport + "/" + directory.getName(), copySubDirectory, true);
                    } else {
                        opSucces = copyDirectory(subdirectory, pathExport, copySubDirectory, true);
                    }
                    if (!opSucces) {
                        succes = false;
                    }
                }
            }
            return succes;
        } else {
            return false;
        }
    }
