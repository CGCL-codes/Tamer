    public void addDicomRole(String rolename) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(dicomRolesFile, true));
            Role role = new Role(rolename);
            role.setDicomRole(true);
            JSONObject jsonObject = JSONObject.fromObject(role);
            writer.write(jsonObject.toString());
            writer.newLine();
        } catch (IOException e) {
            log.error("Can't add dicom role to roles file!", e);
        } finally {
            close(writer, "roles file reader");
        }
    }
