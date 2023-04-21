    public void close() throws IOException {
        out.write(dg.digest());
        this.flush();
        out.close();
    }
