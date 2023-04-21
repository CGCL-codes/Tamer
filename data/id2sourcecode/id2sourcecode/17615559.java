    public static void main(String[] args) {
        String filename = "/home/randres/Escritorio/kindle/books/Rice, Anne - La voz del diablo.prc";
        String html = "/home/randres/Escritorio/fich.html";
        try {
            MyReader reader = new MyReader(filename);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(html), reader.getEncoding()));
            while (reader.hasNext()) {
                String next = reader.getNext();
                writer.write(next);
                writer.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
