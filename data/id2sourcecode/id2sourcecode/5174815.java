    public static void main(String[] args) throws Exception {
        YamlConfig config = new YamlConfig();
        config.writeConfig.setAutoAnchor(true);
        YamlReader reader = new YamlReader(new FileReader("test/test.yml"));
        YamlWriter writer = new YamlWriter(new OutputStreamWriter(System.out));
        writer.write(reader.read());
    }
