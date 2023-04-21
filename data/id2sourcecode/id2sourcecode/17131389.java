    public void testConverterWithPathInHyphenStyle() throws Exception {
        Style style = new HyphenStyle();
        Format format = new Format(style);
        Strategy strategy = new AnnotationStrategy();
        Persister persister = new Persister(strategy, format);
        ServerDetails primary = new ServerDetails("host1.blah.com", 4567, "PRIMARY");
        ServerDetails secondary = new ServerDetails("host2.foo.com", 4567, "SECONDARY");
        ServerDetailsReference reference = new ServerDetailsReference(primary, secondary);
        StringWriter writer = new StringWriter();
        persister.write(reference, writer);
        System.out.println(writer);
        ServerDetailsReference recovered = persister.read(ServerDetailsReference.class, writer.toString());
        assertEquals(recovered.getPrimary().getHost(), reference.getPrimary().getHost());
        assertEquals(recovered.getPrimary().getPort(), reference.getPrimary().getPort());
        assertEquals(recovered.getPrimary().getName(), reference.getPrimary().getName());
        assertEquals(recovered.getSecondary().getHost(), reference.getSecondary().getHost());
        assertEquals(recovered.getSecondary().getPort(), reference.getSecondary().getPort());
        assertEquals(recovered.getSecondary().getName(), reference.getSecondary().getName());
    }
