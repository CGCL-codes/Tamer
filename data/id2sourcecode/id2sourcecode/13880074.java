    public Object call(Context context, List args) throws FunctionCallException {
        checkArgs(args, 1);
        Function stringFunction = getFunction(context, "string");
        String filename = (String) stringFunction.call(context, args);
        try {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            URL url = cl.getResource(filename);
            if (url == null) url = new URL(filename);
            InputStream input = url.openStream();
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            int size;
            byte[] buf = new byte[512];
            while ((size = input.read(buf)) >= 0) output.write(buf, 0, size);
            return output.toString();
        } catch (Exception e) {
            SinaxeErrorHandler.warning(name + "() failed to open file '" + filename + "'!", e);
        }
        return null;
    }
