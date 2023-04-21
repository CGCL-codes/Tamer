    public static <S extends OutputStream> S convertToOutputStream(InputStream in, Class<S> outClass) throws Exception {
        OutputStream out = outClass.newInstance();
        while (in.available() != 0) out.write(in.read());
        return outClass.cast(out);
    }
