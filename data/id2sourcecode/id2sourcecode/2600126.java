    private void saveRules() throws IOException {
        FileWriter fw = new FileWriter("consolerules");
        for (int i = 0; i < readOnlyRules.size(); i++) {
            fw.write(readOnlyRules.get(i) + "\r\n");
        }
        for (int i = 0; i < rules.size(); i++) {
            fw.write(rules.get(i) + "\r\n");
        }
        fw.close();
    }
