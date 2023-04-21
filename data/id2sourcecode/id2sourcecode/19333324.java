        public void newEntry(String pString) throws IOException {
            if (entry != null) {
                mzos.closeEntry();
            }
            mzos.putNextEntry(entry = new ZipEntry(pString));
        }
