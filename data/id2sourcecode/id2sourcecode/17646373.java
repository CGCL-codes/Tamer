    public void s_testOtherTextWithPath() throws Exception {
        Persister persister = new Persister();
        OtherPathWithTextAndElementExample example = new OtherPathWithTextAndElementExample("T", "A", "E");
        StringWriter writer = new StringWriter();
        persister.write(example, writer);
        OtherPathWithTextAndElementExample recovered = persister.read(OtherPathWithTextAndElementExample.class, writer.toString());
        assertEquals(recovered.a, example.a);
        assertEquals(recovered.b, example.b);
        assertEquals(recovered.c, example.c);
        validate(persister, example);
    }
