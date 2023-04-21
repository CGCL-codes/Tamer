            @Override
            public int read(byte[] arg0, int arg1, int arg2) throws IOException {
                int read = super.read(arg0, arg1, arg2);
                if (read > 0) bos.write(arg0, arg1, read);
                return read;
            }
