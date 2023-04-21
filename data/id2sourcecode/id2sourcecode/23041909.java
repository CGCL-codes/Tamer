    public void storeSearchResults(String searchName, ProgramList results) {
        Map search = (Map) m_searches.get(searchName);
        int count = results.getLength();
        Vector storedResults = new Vector(count);
        Programs programs = Programs.getInstance();
        for (int i = 0; i < count; i++) {
            ProgItem result = (ProgItem) results.item(i);
            String[] program = new String[2];
            program[0] = programs.getChannel(result);
            program[1] = programs.getStartTime(result);
            storedResults.add(program);
        }
        search.put(RESULTS, storedResults);
    }
