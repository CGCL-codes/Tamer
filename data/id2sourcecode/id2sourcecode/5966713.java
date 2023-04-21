    public static byte[] load(String text) throws FileNotFoundException {
        final ValueHolder<ByteArrayOutputStream> result = new Structures.ValueHolder<ByteArrayOutputStream>();
        new Refactor.FileTx<FileInputStream>(new FileInputStream(text)) {

            @Override
            protected void internalExecute(FileInputStream input) throws IOException {
                ByteArrayOutputStream r = new ByteArrayOutputStream();
                byte[] b = new byte[4096];
                int len;
                while ((len = input.read(b)) != -1) r.write(b, 0, len);
                result.set(r);
            }
        }.execute();
        return result.get().toByteArray();
    }
