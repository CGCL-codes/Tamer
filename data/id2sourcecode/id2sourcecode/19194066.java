    public void testStyleDup() throws Exception {
        Style style = new CamelCaseStyle();
        Format format = new Format(style);
        TextExample example = new TextExample("a", "b");
        Persister persister = new Persister(format);
        StringWriter writer = new StringWriter();
        persister.write(example, writer);
        System.out.println(writer);
        TextExample restored = persister.read(TextExample.class, writer.toString());
        assertEquals(example.a, restored.a);
        assertEquals(example.b, restored.b);
    }
