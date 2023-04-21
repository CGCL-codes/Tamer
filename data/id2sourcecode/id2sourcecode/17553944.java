    public boolean save() {
        FileOutputStream stream;
        BufferedWriter writer;
        root.put("mapRadius", mapRadius);
        root.put("defaultEnter", defaultEnter);
        root.put("defaultBuild", defaultBuild);
        root.put("defaultDestroy", defaultDestroy);
        root.put("enableRadius", enableRadius);
        root.put("enableHeroChat", enableHeroChat);
        root.put("zoneTool", zoneTool);
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
            return false;
        }
        return true;
    }
