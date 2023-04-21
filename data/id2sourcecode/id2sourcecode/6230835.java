    private static void Save(File file) {
        Yaml yaml = new Yaml();
        HashMap<String, Object> root = new HashMap<String, Object>();
        FileOutputStream stream;
        BufferedWriter writer;
        HashSet<String> savedEntrances = new HashSet<String>();
        int count = 1;
        for (String key : Current.MapEntrances.keySet()) {
            MapEntrance me = Current.MapEntrances.get(key);
            if (me != null) {
                if (me.getMap() != null) {
                    if (!savedEntrances.contains(me.getSignLocation())) {
                        HashMap<String, Object> newEnt = new HashMap<String, Object>();
                        newEnt.put("MapName", me.getMap().getMapName());
                        newEnt.put("SignLocation", me.getSignLocation());
                        newEnt.put("ChestLocation", me.getChestLocation());
                        newEnt.put("HighScores", me.getHighScores());
                        root.put("Entrance" + count, newEnt);
                        savedEntrances.add(me.getSignLocation());
                        count++;
                    }
                }
            }
        }
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
