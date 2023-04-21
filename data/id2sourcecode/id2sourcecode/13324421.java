            private void addFileEntry(ZipOutputStream zipStream, String path, TFile file) throws IOException {
                ZipEntry entry = new ZipEntry(path + file.name());
                zipStream.putNextEntry(entry);
                file.copyTo(zipStream);
                zipStream.closeEntry();
            }
