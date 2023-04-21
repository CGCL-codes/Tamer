    public void save() {
        FileOutputStream stream;
        BufferedWriter writer;
        root.put("mapRadius", mapRadius);
        root.put("defaultEnter", defaultEnter);
        root.put("defaultBuild", defaultBuild);
        root.put("defaultDestroy", defaultDestroy);
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
        }
    }
