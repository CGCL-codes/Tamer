    public void execute(Reader reader, Writer writer, Properties htmlCleanerConfig) throws IOException {
        if (htmlCleanerConfig != null) {
            setConfiguration(htmlCleanerConfig);
        }
        TagNode node = cleaner.clean(reader);
        CleanerProperties cleanerProps = cleaner.getProperties();
        PrettyXmlSerializer xmlSerializer = new PrettyXmlSerializer(cleanerProps);
        xmlSerializer.writeXml(node, writer, "utf-8");
    }
