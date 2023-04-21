    public boolean save() {
        FileOutputStream stream;
        BufferedWriter writer;
        root.put("reteleportDelay", reteleportDelay);
        root.put("permissionSystem", permissionSystem);
        Map<String, Object> worldMap = new HashMap<String, Object>();
        for (EpicGatesWorld egw : additionalWorlds) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("environment", egw.environment.toString());
            worldMap.put(egw.name, map);
        }
        root.put("additionalWorlds", worldMap);
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
