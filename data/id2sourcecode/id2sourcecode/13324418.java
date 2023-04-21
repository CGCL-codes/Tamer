    public void zipTo(TFile file) throws TIoException {
        file.write(new OutputProcessor() {

            public void process(OutputManager outputManager) throws IOException {
                ZipOutputStream zipStream = new ZipOutputStream(outputManager.outputStream());
                outputManager.registerResource(zipStream);
                addDirEntry(zipStream, "", TDirectory.this);
            }

            private void addDirEntry(ZipOutputStream zipStream, String path, TDirectory directory) throws IOException {
                TFile[] files = directory.listFiles();
                for (int i = 0; i < files.length; i++) {
                    addFileEntry(zipStream, path, files[i]);
                }
                TDirectory[] directories = directory.listDirs();
                for (int i = 0; i < directories.length; i++) {
                    TDirectory subDirectory = directories[i];
                    addDirEntry(zipStream, path + "/" + subDirectory.name(), subDirectory);
                }
                zipStream.putNextEntry(new ZipEntry(path + "/"));
                zipStream.closeEntry();
            }

            private void addFileEntry(ZipOutputStream zipStream, String path, TFile file) throws IOException {
                ZipEntry entry = new ZipEntry(path + file.name());
                zipStream.putNextEntry(entry);
                file.copyTo(zipStream);
                zipStream.closeEntry();
            }
        });
    }
