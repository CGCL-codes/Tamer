    public void addRole(Role role) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(rolesMappingFile, true));
            JSONObject jsonObject = JSONObject.fromObject(role);
            writer.write(jsonObject.toString());
            writer.newLine();
        } catch (IOException e) {
            log.error("Can't add role to roles mapping file!", e);
        } finally {
            close(writer, "mapping file reader");
        }
    }
