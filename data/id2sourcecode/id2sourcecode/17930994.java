    static byte[] getQuery() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BufferedReader reader = new BufferedReader(new FileReader("INI_request.xml"));
        while (reader.ready()) {
            out.write((reader.readLine() + "\n").getBytes());
        }
        reader.close();
        System.out.println("----------REQUETE");
        System.out.println(new String(out.toByteArray()));
        return out.toByteArray();
    }
