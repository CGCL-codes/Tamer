    public void addAETGroup(AETGroup aetGroup) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(aetGroupsFile, true));
            JSONObject jsonObject = JSONObject.fromObject(aetGroup);
            writer.write(jsonObject.toString());
            writer.newLine();
        } catch (IOException e) {
            log.error("Can't add aet group to aet groups file!", e);
        } finally {
            close(writer, "aet groups file reader");
        }
    }
