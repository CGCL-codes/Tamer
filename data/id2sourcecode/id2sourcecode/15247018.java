    public Object call(Context context, List args) throws FunctionCallException {
        checkArgs(args, 2);
        Function stringFunction = getFunction(context, "string");
        String filename = (String) stringFunction.call(context, args.subList(0, 1));
        Navigator nav = context.getNavigator();
        Object node = args.get(1);
        if (node instanceof List && !((List) node).isEmpty()) node = ((List) node).get(0);
        if (nav.isElement(node) || nav.isDocument(node)) return writeXML(filename, node);
        String data = (String) stringFunction.call(context, args.subList(1, 2));
        try {
            ByteArrayInputStream input = new ByteArrayInputStream(data.getBytes());
            OutputStream output = new FileOutputStream(filename);
            int size;
            byte[] buf = new byte[512];
            while ((size = input.read(buf)) >= 0) output.write(buf, 0, size);
            return Boolean.TRUE;
        } catch (Exception e) {
            callException("failed to open file '" + filename + "'", e);
        }
        return Boolean.FALSE;
    }
