    protected void converse() throws IOException {
        System.out.println("Ready to read and write port.");
        if (is != null) {
            is.close();
        }
        os.close();
    }
