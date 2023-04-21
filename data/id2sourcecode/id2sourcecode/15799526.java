    private static void Save(File file, GameState gs) {
        Yaml yaml = new Yaml();
        HashMap<String, Object> root = new HashMap<String, Object>();
        FileOutputStream stream;
        BufferedWriter writer;
        HashSet<String> completedObjectives = new HashSet<String>();
        root.put("MapName", gs.getMap().getMapName());
        root.put("WorldName", gs.getWorld().getName());
        root.put("PlayerName", gs.getPlayer().getName());
        root.put("Score", gs.getScore());
        root.put("DeathCount", gs.getDeathCount());
        root.put("EntryPoint", Util.GetStringFromLocation(gs.getEntryPoint()));
        for (String obj : gs.getCompletedObjectives()) {
            completedObjectives.add(obj);
        }
        root.put("CompletedObjectives", completedObjectives);
        root.put("BlocksBroken", gs.getBlocksBroken());
        root.put("BlocksPlaced", gs.getBlocksPlaced());
        root.put("BlocksPlacedDistance", gs.getBlocksPlacedDistance());
        root.put("ItemsCrafted", gs.getItemsCrafted());
        root.put("Rewards", gs.getRewards());
        root.put("MapContents", gs.getMapContents());
        root.put("InventoryBuffer", gs.getInventoryBuffer());
        root.put("InChallenge", gs.getInChallenge());
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
