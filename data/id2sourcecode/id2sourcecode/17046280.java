    public boolean save() {
        FileOutputStream stream;
        BufferedWriter writer;
        root.put("reteleportDelay", reteleportDelay);
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
