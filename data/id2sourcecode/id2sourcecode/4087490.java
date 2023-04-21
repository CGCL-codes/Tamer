    public long getPosition() throws IOException {
        long position = 0;
        if (this.out != null) {
            this.out.flush();
        }
        if (this.fos != null) {
            this.fos.flush();
            position = this.fos.getChannel().position();
        }
        return position;
    }
