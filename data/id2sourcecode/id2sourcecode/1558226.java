    public void xtest1() throws Exception {
        Digester digester = new BasicDigester(DigestType.SHA_256);
        Digester delegate = new HexDigester(digester);
        System.out.println(new String(delegate.digest("Teste da Silva Sauro\n".getBytes())));
    }
