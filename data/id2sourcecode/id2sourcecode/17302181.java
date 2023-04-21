    public void setFlushingOn() throws IOException {
        if (gotwriter) throw new IOException("You have already requested a Writer, " + "and can't change it's properties now");
        flushing = true;
    }
