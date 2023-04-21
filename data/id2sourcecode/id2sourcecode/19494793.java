    public void testNoStyle() throws Exception {
        CaseExample example = new CaseExample("a", "b");
        Persister persister = new Persister();
        StringWriter writer = new StringWriter();
        persister.write(example, writer);
        System.out.println(writer);
        CaseExample restored = persister.read(CaseExample.class, writer.toString());
        assertEquals(example.a, restored.a);
        assertEquals(example.b, restored.b);
    }
