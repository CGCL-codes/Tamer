    public String receive() throws IOException {
        if (in == null) {
            in = connection.openInputStream();
        }
        message("Receiving...");
        ByteArrayOutputStream out1 = new ByteArrayOutputStream();
        int read;
        while ((read = in.read()) != -1 && read != EndOfStream) {
            out1.write(read);
        }
        String result = new String(out1.toByteArray(), "UTF-8");
        out1.close();
        message("\"" + result + "\" empfangen");
        System.out.println("\"" + result + "\" empfangen");
        return result;
    }
