    protected void enableIO() {
        if (this.readEnd == null) {
            this.readEnd = new PipedReader();
            this.writeEnd = new PipedWriter();
            try {
                readEnd.connect(writeEnd);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
