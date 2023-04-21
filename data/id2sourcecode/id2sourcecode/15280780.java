    public void run() {
        while (true) {
            try {
                this.os.write(this.is.read());
            } catch (final IOException e) {
                break;
            }
        }
    }
