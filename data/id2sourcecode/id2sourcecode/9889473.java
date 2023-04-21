    public void testWrapper() throws Exception {
        Registry registry = new Registry();
        Strategy strategy = new RegistryStrategy(registry);
        Serializer serializer = new Persister(strategy);
        Entry entry = new Entry("name", "value");
        Wrapper wrapper = new Wrapper(entry);
        WrapperExample example = new WrapperExample(wrapper);
        WrapperConverter converter = new WrapperConverter(serializer);
        StringWriter writer = new StringWriter();
        registry.bind(Wrapper.class, converter);
        serializer.write(example, writer);
        serializer.read(WrapperExample.class, writer.toString());
        System.err.println(writer.toString());
    }
