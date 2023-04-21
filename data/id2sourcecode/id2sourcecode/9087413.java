        public OutputStream getOutputStream(OutputStream former, String newFile, int mode) throws IOException {
            if (former != null) {
                assert former == output;
                output.closeEntry();
            }
            ZipEntry newEntry = new ZipEntry("flexotask-runtime/" + newFile);
            if (mode != 0) {
                newEntry.setUnixMode(mode);
            }
            output.putNextEntry(newEntry);
            return output;
        }
