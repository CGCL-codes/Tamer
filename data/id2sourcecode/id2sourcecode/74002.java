    public String receive(StreamConnection con) throws IOException {
        if (in == null) {
            in = con.openInputStream();
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int read;
        while ((read = in.read()) != -1 && read != this.EndOfStream) {
            out.write(read);
            System.out.println(new String(out.toByteArray(), "UTF-8"));
        }
        System.out.println("Debug1");
        String result = new String(out.toByteArray(), "UTF-8");
        out.close();
        return result;
    }
