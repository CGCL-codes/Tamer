    public WritableByteChannel getChannel() {
        if (streams == null || streams.size() != 1) return null;
        OutputStream os = streams.get(0);
        if (os instanceof FileOutputStream) return ((FileOutputStream) os).getChannel();
        if (os instanceof MultiOutputStream) return ((MultiOutputStream) os).getChannel();
        if (os instanceof HTTPOutputStream) return ((HTTPOutputStream) os).getChannel();
        if (os instanceof MaxSizeOutputStream) return null;
        System.err.println("*** Unknown outputstream type: " + os.getClass().getName());
        return null;
    }
