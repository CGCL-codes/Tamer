        public OutputStream getOutputStream() throws IOException {
            if (!writeable) throw new IOException("File readonly: " + coerceToString());
            try {
                return new FileOutputStream(path);
            } catch (FileNotFoundException e) {
                return null;
            }
        }
