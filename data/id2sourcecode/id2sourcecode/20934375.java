    private void save() {
        checkFile();
        try {
            FileOutputStream stream = new FileOutputStream(file);
            stream.getChannel().truncate(0);
            Writer writer = new BufferedWriter(new OutputStreamWriter(stream));
            Location val;
            long worldId, x, y, z;
            String line;
            try {
                for (Map.Entry<String, Location> home : homes.entrySet()) {
                    val = home.getValue();
                    worldId = val.getWorld().getId();
                    x = val.getBlockX();
                    y = val.getBlockY();
                    z = val.getBlockZ();
                    line = String.format("%s : %s %d %d %d\n", home.getKey(), Long.toString(worldId, 16), x, y, z);
                    writer.write(line);
                }
            } finally {
                writer.close();
            }
            fileTime = file.lastModified();
        } catch (IOException e) {
        }
    }
