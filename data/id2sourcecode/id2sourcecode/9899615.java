    public void compress(Reader reader, Writer writer) throws CompressorException {
        try {
            Object reporter = Proxy.newProxyInstance(reporterClass.getClassLoader(), new Class<?>[] { reporterClass }, new ErrorReporterProxy());
            Constructor<?> con = compressorClass.getConstructor(Reader.class, reporterClass);
            Object compressor = con.newInstance(reader, reporter);
            Method meth = compressorClass.getMethod("compress", Writer.class, int.class, boolean.class, boolean.class, boolean.class, boolean.class);
            meth.invoke(compressor, writer, 1000, true, false, false, false);
        } catch (InvocationTargetException x) {
            throw new CompressorException("Error during compression", x);
        } catch (Exception x) {
            throw new CompressorException("Can't invoke compressor", x);
        }
    }
