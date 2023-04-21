    public void testSectionIndex() throws Exception {
        Persister persister = new Persister();
        PathSectionIndexExample example = new PathSectionIndexExample("12345", "67890");
        StringWriter writer = new StringWriter();
        persister.write(example, System.err);
        persister.write(example, writer);
        PathSectionIndexExample recovered = persister.read(PathSectionIndexExample.class, writer.toString());
        assertEquals(recovered.home, example.home);
        assertEquals(recovered.mobile, example.mobile);
        assertElementExists(writer.toString(), "/pathSectionIndexExample/contact-details[1]/phone/mobile");
        assertElementExists(writer.toString(), "/pathSectionIndexExample/contact-details[2]/phone/home");
        validate(example, persister);
    }
