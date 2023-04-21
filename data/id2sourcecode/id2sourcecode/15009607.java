    public void testBringPrefixInScope() throws Exception {
        Persister persister = new Persister();
        PathWithPrefixOutOfScope example = new PathWithPrefixOutOfScope("tim");
        BringPrefixInScope parent = new BringPrefixInScope(example);
        StringWriter writer = new StringWriter();
        persister.write(parent, writer);
        System.out.println(writer);
        BringPrefixInScope recovered = persister.read(BringPrefixInScope.class, writer.toString());
        assertEquals(recovered.example.getId(), "tim");
        validate(persister, parent);
    }
