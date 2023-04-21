    public void addToSchedule(ProgItem prog) {
        String[] program = new String[2];
        Programs programs = Programs.getInstance();
        program[0] = programs.getChannel(prog);
        program[1] = programs.getStartTime(prog);
        m_schedule.add(program);
    }
