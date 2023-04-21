            @Override
            protected void internalExecute(FileInputStream input) throws IOException {
                ByteArrayOutputStream r = new ByteArrayOutputStream();
                byte[] b = new byte[4096];
                int len;
                while ((len = input.read(b)) != -1) r.write(b, 0, len);
                result.set(r);
            }
