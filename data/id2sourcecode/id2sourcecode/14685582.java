    public void storeFavoritesResults(ProgramList results) {
        int count = (results != null ? results.getLength() : 0);
        m_results = new Vector(count);
        Programs programs = Programs.getInstance();
        for (int i = 0; i < count; i++) {
            ProgItem result = (ProgItem) results.item(i);
            String[] program = new String[2];
            program[0] = Programs.getInstance().getChannel(result);
            program[1] = Programs.getInstance().getStartTime(result);
            m_results.add(program);
        }
    }
