    protected InputStream loadStream() throws IOException {
        if (xload || xdump) {
            return new FileInputStream(xFileName + "-" + getDumpName() + ".ser");
        } else {
            return new ByteArrayInputStream(bao.toByteArray());
        }
    }
