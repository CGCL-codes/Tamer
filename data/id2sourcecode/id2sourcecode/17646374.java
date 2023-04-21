    public void s_testTextWithTextPath() throws Exception {
        Persister persister = new Persister();
        PathWithMultipleTextExample example = new PathWithMultipleTextExample("A", "B", "C", "D");
        StringWriter writer = new StringWriter();
        persister.write(example, writer);
        PathWithMultipleTextExample recovered = persister.read(PathWithMultipleTextExample.class, writer.toString());
        assertEquals(recovered.a, example.a);
        assertEquals(recovered.b, example.b);
        assertEquals(recovered.c, example.c);
        assertEquals(recovered.d, example.d);
        validate(persister, example);
    }
