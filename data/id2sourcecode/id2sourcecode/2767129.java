    public boolean checkInSchedule(ProgItem prog) {
        String[] program = new String[2];
        Programs programs = Programs.getInstance();
        program[0] = programs.getChannel(prog);
        program[1] = programs.getStartTime(prog);
        return m_schedule.contains(program);
    }
