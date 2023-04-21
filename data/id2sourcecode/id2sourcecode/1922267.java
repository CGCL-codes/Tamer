    public void process(ClasspathResource resource, StringWriter writer) {
        ResourceReader reader = new ResourceReader(resource);
        ExampleParser parser = Parboiled.createParser(ExampleParser.class);
        PegDownProcessor processor = new PegDownProcessor(parser);
        writer.write(processor.markdownToHtml(reader.toCharArray()));
    }
