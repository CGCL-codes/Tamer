    public void testTextWithPath() throws Exception {
        Persister persister = new Persister();
        PathWithTextAndElementExample example = new PathWithTextAndElementExample("T", "A", "E");
        StringWriter writer = new StringWriter();
        persister.write(example, writer);
        PathWithTextAndElementExample recovered = persister.read(PathWithTextAndElementExample.class, writer.toString());
        assertEquals(recovered.value, example.value);
        assertEquals(recovered.text, example.text);
        assertEquals(recovered.item, example.item);
        validate(persister, example);
    }
