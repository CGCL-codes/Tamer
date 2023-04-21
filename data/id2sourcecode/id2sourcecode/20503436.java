    public void testPersonConverter() throws Exception {
        Style style = new CamelCaseStyle();
        Format format = new Format(style);
        Registry registry = new Registry();
        Person person = new Person("Niall", 30);
        RegistryStrategy strategy = new RegistryStrategy(registry);
        Serializer serializer = new Persister(strategy, format);
        Converter converter = new PersonConverter(serializer);
        StringWriter writer = new StringWriter();
        registry.bind(Person.class, converter);
        PersonProfile profile = new PersonProfile(person);
        serializer.write(profile, writer);
        System.out.println(writer.toString());
        PersonProfile read = serializer.read(PersonProfile.class, writer.toString());
        assertEquals(read.person.name, "Niall");
        assertEquals(read.person.age, 30);
    }
