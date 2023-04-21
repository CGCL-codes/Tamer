            public void next(String path) throws IOException {
                zos.putNextEntry(new ZipEntry(path));
            }
