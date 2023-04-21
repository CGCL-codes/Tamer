    public void testWriteOutputStream() {
        OntModel m = ModelFactory.createOntologyModel();
        OntClass A = m.createClass(NS + "A");
        OntClass B = m.createClass(NS + "B");
        OntClass C = m.createClass(NS + "C");
        OntClass D = m.createClass(NS + "D");
        A.addSubClass(B);
        B.addSubClass(C);
        B.addSubClass(D);
        ObjectProperty p = m.createObjectProperty(NS + "p");
        p.addDomain(A);
        p.addRange(B);
        p.addRange(C);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        m.write(out);
        String s = out.toString();
        ByteArrayInputStream in = new ByteArrayInputStream(s.getBytes());
        Model mIn1 = ModelFactory.createDefaultModel();
        mIn1.read(in, BASE);
        Model mIn2 = ModelFactory.createDefaultModel();
        mIn2.read(new ByteArrayInputStream(DOC.getBytes()), BASE);
        assertTrue("InputStream write/read cycle failed (1)", mIn1.isIsomorphicWith(m.getBaseModel()));
        assertTrue("InputStream write/read cycle failed (2)", mIn2.isIsomorphicWith(m.getBaseModel()));
    }
