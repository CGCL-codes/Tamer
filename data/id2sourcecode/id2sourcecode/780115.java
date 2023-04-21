    private static void printGood() throws IOException {
        URL url = NoParameterHierarchy.class.getResource("noParamHier-good.xml");
        Configuration config = ConfigurationBuilder.newUrlConfig(url);
        System.out.println("\n\nA better way:");
        System.out.println(IOUtils.toString(url.openStream()));
        System.out.println("\nAs properties:");
        config.save(System.out, ConfigFormats.PROPERTIES);
        System.out.println("\nAccess and print:");
        for (String name : config.extend().getList("module.name")) {
            String prefix = "module." + name + ".";
            System.out.println(config.extend().getClass(prefix + "class"));
            System.out.println(config.getInt(prefix + "timeout"));
        }
    }
