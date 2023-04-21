    public void testDup() throws Exception {
        Example example = new Example("a", "b");
        Persister persister = new Persister();
        StringWriter writer = new StringWriter();
        persister.write(example, writer);
        System.out.println(writer);
        Example restored = persister.read(Example.class, writer.toString());
        assertEquals(example.a, restored.a);
        assertEquals(example.b, restored.b);
    }
