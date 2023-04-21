        @Override
        public OutputStream openOutputStream() {
            try {
                ZipEntry entry = new ZipEntry(getName());
                zipOut.putNextEntry(entry);
                return zipOut;
            } catch (Exception ex) {
                System.out.println("Error acquiring output stream for binary data");
                ex.printStackTrace();
                return null;
            }
        }
