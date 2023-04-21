    @Override
    public void run() {
        try {
            for (int b = is.read(); b != -1; b = is.read()) os.write(b & 0xFF);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                os.flush();
                os.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
