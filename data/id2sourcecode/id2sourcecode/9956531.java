    private void addToZip(String path, String srcFile, ZipOutputStream zipOutputStream) {
        File file = new File(srcFile);
        if (file.isDirectory()) {
            addFolderToZip(path, srcFile, zipOutputStream);
        } else {
            byte[] lenghtBuffer = new byte[1024];
            int lenghtBytes;
            try {
                FileInputStream fileInputStream = new FileInputStream(srcFile);
                ZipEntry zipEntry = new ZipEntry(srcFile);
                new PrintStream(byteArrayOutputStream);
                zipOutputStream.putNextEntry(zipEntry);
                while ((lenghtBytes = fileInputStream.read(lenghtBuffer)) > 0) {
                    zipOutputStream.write(lenghtBuffer, 0, lenghtBytes);
                    sizeCurrentBufferFile += lenghtBytes;
                }
                zipOutputStream.closeEntry();
                progressBar.getDisplay().syncExec(new Runnable() {

                    public void run() {
                        progressBar.setSelection(cont++);
                    }
                });
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
