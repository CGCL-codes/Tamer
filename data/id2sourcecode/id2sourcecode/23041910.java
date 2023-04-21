    public ProgramList recallStoredSearchResults(String searchName, ProgramList inputNodes) {
        Map search = (Map) m_searches.get(searchName);
        Programs programs = Programs.getInstance();
        ProgramList fullResults = programs.getEmptyProgramList();
        Vector storedResults = (Vector) search.get(RESULTS);
        Date now = new Date();
        if (storedResults != null) {
            TreeSet sortedResults = new TreeSet(new ResultComparitor());
            sortedResults.addAll(storedResults);
            int count = inputNodes.getLength();
            for (int i = 0; i < count; i++) {
                ProgItem prog = (ProgItem) inputNodes.item(i);
                String[] program = new String[2];
                program[0] = programs.getChannel(prog);
                program[1] = programs.getStartTime(prog);
                if (getSearchFromNow()) {
                    Calendar startTime = Utilities.makeCal(program[1]);
                    if (startTime.getTime().compareTo(now) < 0) continue;
                }
                if (sortedResults.contains(program)) {
                    fullResults.add(prog);
                }
            }
        }
        return fullResults;
    }
