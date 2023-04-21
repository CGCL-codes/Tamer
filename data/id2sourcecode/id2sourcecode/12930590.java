    private void teste(String res) throws Exception {
        InputStream in1 = Main.class.getResourceAsStream(res);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buf = new byte[2048];
        int i = -1;
        while ((i = in1.read(buf)) > -1) out.write(buf, 0, i);
        ByteArrayInputStream in2 = new ByteArrayInputStream(out.toByteArray());
        Reader r1 = new InputStreamReader(in2);
        Lexico lex = new Lexico();
        lex.setIgnoreComment(true);
        lex.setIgnoreSpace(true);
        List<Token> tokens1 = lex.analizar(r1);
        for (Token to : tokens1) System.out.println(to);
    }
