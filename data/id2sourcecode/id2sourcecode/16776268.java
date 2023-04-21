    @Override
    public void append(String name, Readable readable) {
        writer.write(readable.toString() + "\n");
    }
