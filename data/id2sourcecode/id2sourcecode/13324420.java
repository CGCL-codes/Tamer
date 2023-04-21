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
