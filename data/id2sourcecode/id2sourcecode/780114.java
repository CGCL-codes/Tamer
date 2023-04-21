    private static void printBad() throws IOException {
        URL url = NoParameterHierarchy.class.getResource("noParamHier-bad.xml");
        Configuration config = ConfigurationBuilder.newUrlConfig(url);
        System.out.println("Original:");
        System.out.println(IOUtils.toString(url.openStream()));
        System.out.println("\nHow conga sees it:");
        config.save(System.out);
        System.out.println("\nOr, as properties:");
        config.save(System.out, ConfigFormats.PROPERTIES);
    }
