    private void updateMemory(Trail t) {
        for (int i = 0; i < memory.length; i++) {
            if (memory[i] == t) {
                for (int j = i; j < (memory.length - 1); j++) {
                    memory[j] = memory[j + 1];
                }
                memory[memory.length - 1] = null;
            }
        }
        if (t.getCost() != null) {
            for (int i = 0; i < memory.length; i++) {
                if (memory[i] == null) {
                    memory[i] = t;
                    break;
                } else if ((t.getCost().compareTo(memory[i].getCost()) == 1) || ((t.getCost().compareTo(memory[i].getCost()) == 0) && (t.getVertices().size() < memory[i].getVertices().size()))) {
                    for (int j = (memory.length - 1); j > i; j--) {
                        memory[j] = memory[j - 1];
                    }
                    memory[i] = t;
                    break;
                }
            }
        }
    }
