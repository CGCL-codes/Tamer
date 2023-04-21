    public void write(LogReader reader) throws IOException {
        while (true) {
            try {
                Message next = reader.read();
                if (next == null) return;
                write(next);
            } catch (ParseException pe) {
                pe.printStackTrace();
            }
        }
    }
