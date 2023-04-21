    public void testWriteWriter() {
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
        StringWriter out = new StringWriter();
        m.write(out);
        String s = out.toString();
        Model mIn1 = ModelFactory.createDefaultModel();
        mIn1.read(new StringReader(s), BASE);
        Model mIn2 = ModelFactory.createDefaultModel();
        mIn2.read(new StringReader(DOC), BASE);
        assertTrue("Writer write/read cycle failed (1)", mIn1.isIsomorphicWith(m.getBaseModel()));
        assertTrue("Writer write/read cycle failed (2)", mIn2.isIsomorphicWith(m.getBaseModel()));
    }
