    public static void Save() {
        Yaml yaml = new Yaml();
        HashMap<String, Object> root = new HashMap<String, Object>();
        FileOutputStream stream;
        BufferedWriter writer;
        root.put("enableRadius", enableRadius);
        root.put("enableHeroChat", enableHeroChat);
        root.put("globalZoneDefaultBuild", globalZoneDefaultBuild);
        root.put("globalZoneDefaultDestroy", globalZoneDefaultDestroy);
        root.put("globalZoneDefaultEnter", globalZoneDefaultEnter);
        root.put("zoneTool", zoneTool);
        root.put("language", language);
        root.put("enableSpout", enableSpout);
        try {
            stream = new FileOutputStream(file);
            stream.getChannel().truncate(0);
            writer = new BufferedWriter(new OutputStreamWriter(stream));
            try {
                writer.write(yaml.dump(root));
            } finally {
                writer.close();
            }
        } catch (IOException e) {
            Log.Write(e.getMessage());
        }
    }
