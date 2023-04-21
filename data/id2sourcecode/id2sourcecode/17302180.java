    public void setBufferingOff() throws IOException {
        if (gotwriter) throw new IOException("You have already requested a Writer, " + "and can't change it's properties now");
        buffered = false;
    }
