    private void zipFileOs(ZipOutputStream os, FileWrapper[] sourceFiles) throws FileNotFoundException, IOException {
        for (FileWrapper file : sourceFiles) {
            if (file.isDirectory()) {
                zipFileOs(os, file.listFiles());
            } else {
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(file.getAbsolutePath());
                    os.putNextEntry(new ZipEntry(file.getPath()));
                    _iou.copyBytes(fis, os);
                } finally {
                    _iou.closeInputStream(fis);
                }
            }
        }
    }
