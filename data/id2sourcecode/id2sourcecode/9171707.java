    public ROM(String id) throws IOException {
        name = new File(id).getName().toUpperCase();
        if (name.lastIndexOf('.') != -1) name = name.substring(0, name.lastIndexOf('.'));
        InputStream in = Main.class.getResourceAsStream("rom/" + id);
        if (in == null) {
            in = Main.class.getResourceAsStream("/" + id);
            if (in == null) {
                in = new FileInputStream(id);
            }
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        for (int b = in.read(); b != -1; b = in.read()) out.write(b);
        in.close();
        setStore(out.toByteArray());
    }
