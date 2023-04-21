    public synchronized int read() throws IOException {
        if (readed >= movieLength) {
            return -1;
        }
        while (readed >= writed) {
            wait_for_writer();
        }
        return movieData[readed++] + 128;
    }
