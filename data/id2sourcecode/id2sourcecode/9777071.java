    private void addFolder(ZipOutputStream out, String basePath, File dir) throws IOException {
        File[] files = dir.listFiles();
        String newBasePath;
        if (basePath.trim().equals("")) {
            newBasePath = dir.getName();
        } else {
            newBasePath = basePath + SEPARATOR + dir.getName();
        }
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                addFolder(out, newBasePath, files[i]);
            } else {
                addFile(out, newBasePath, files[i]);
            }
        }
        if (files.length == 0) {
            ZipEntry entry = new ZipEntry(newBasePath + SEPARATOR);
            out.putNextEntry(entry);
        }
        currentMessage = "Add Folder: " + dir;
        if (zipMonitor != null) {
            zipMonitor.printMessage();
        }
        if (printToConsoleFlag) {
            System.out.println(currentMessage);
        }
    }
