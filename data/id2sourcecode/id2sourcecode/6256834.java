    private void sortKnowledgeFiles() {
        String sort = "";
        for (int i = 9; i > 0; i--) {
            for (KnowledgeFile current : knowledge.values()) {
                if (current.getSettings().getProperty(msg.RANK, "1").equalsIgnoreCase("" + i) == false) continue;
                String name = current.toString();
                sort = sort.concat(name + ";");
            }
        }
        if (utils.text.isEmpty(sort)) {
            readPriority = new String[] {};
            writePriority = new String[] {};
        } else {
            readPriority = sort.split(";");
            writePriority = readPriority;
        }
    }
