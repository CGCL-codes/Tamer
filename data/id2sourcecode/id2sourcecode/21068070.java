    private int flushStream(InputStream inputStream, OutputStream destination) {
        int reads = 0;
        try {
            int read;
            while ((read = inputStream.read()) != -1) {
                reads++;
                if (destination != null) destination.write(read);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return reads;
    }
