    public void testNestedExample() throws Exception {
        NestedElementExample example = new NestedElementExample("London", "Bevenden", "34512345", "63356464", 61);
        Persister persister = new Persister();
        StringWriter writer = new StringWriter();
        persister.write(example, writer);
        System.out.println(writer.toString());
        NestedElementExample recovered = persister.read(NestedElementExample.class, writer.toString());
        assertEquals(example.city, recovered.city);
        assertEquals(example.street, recovered.street);
        assertEquals(example.mobile, recovered.mobile);
        assertEquals(example.home, recovered.home);
        validate(example, persister);
    }
