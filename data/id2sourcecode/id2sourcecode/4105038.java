    public void write(String fn, BufferedWriter writer) throws IOException {
        if (fn.startsWith("http:")) {
            write(new URL(fn), writer);
        } else {
            BufferedReader reader = new BufferedReader(new FileReader(new File(fn)));
            write(reader, writer);
        }
    }
