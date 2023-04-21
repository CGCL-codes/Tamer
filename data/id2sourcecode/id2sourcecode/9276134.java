    public void testStyleDup() throws Exception {
        Style style = new CamelCaseStyle();
        Format format = new Format(style);
        StyleExample example = new StyleExample("a", "b");
        Persister persister = new Persister(format);
        StringWriter writer = new StringWriter();
        persister.write(example, writer);
        System.out.println(writer);
        StyleExample restored = persister.read(StyleExample.class, writer.toString());
        assertEquals(example.a, restored.a);
        assertEquals(example.b, restored.b);
    }
