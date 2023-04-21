    public ProgramList recallStoredFavoritesResults(ProgramList inputNodes) {
        Programs programs = Programs.getInstance();
        ProgramList fullResults = programs.getEmptyProgramList();
        if (m_results != null && inputNodes != null && fullResults != null) {
            TreeSet sortedResults = new TreeSet(new ResultComparitor());
            sortedResults.addAll(m_results);
            int count = inputNodes.getLength();
            for (int i = 0; i < count; i++) {
                ProgItem prog = (ProgItem) inputNodes.item(i);
                String[] program = new String[2];
                program[0] = Programs.getInstance().getChannel(prog);
                program[1] = Programs.getInstance().getStartTime(prog);
                if (sortedResults.contains(program)) {
                    fullResults.add(prog);
                }
            }
        }
        return fullResults;
    }
