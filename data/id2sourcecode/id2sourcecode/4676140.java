    public void printProperties(Writer writer, UjoTextable ujo) throws IOException {
        printProperties(writer, ujo, ujo.readProperties());
    }
