    public void addGroup(Group group) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(groupsFile, true));
            JSONObject jsonObject = JSONObject.fromObject(group);
            writer.write(jsonObject.toString());
            writer.newLine();
        } catch (IOException e) {
            log.error("Can't add group to groups file!", e);
        } finally {
            close(writer, "groups file reader");
        }
    }
