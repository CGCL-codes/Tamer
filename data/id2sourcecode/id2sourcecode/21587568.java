        public void close() throws IOException {
            super.close();
            byte[] buffer = new byte[4096];
            local.seek(0);
            int read = -1;
            while ((read = local.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            out.close();
            local.close();
        }
