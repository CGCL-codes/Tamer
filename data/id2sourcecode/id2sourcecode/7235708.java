            @Override
            public int read() throws IOException {
                int read = super.read();
                if (read >= 0) bos.write(read);
                return read;
            }
