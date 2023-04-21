    public void display(File file, PrintStream out, DBMapping mapping) throws MimirException {
        write(out, read(file), mapping);
    }
