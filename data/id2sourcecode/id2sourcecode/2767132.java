    public void toggleSchedule(ProgItem prog) {
        String[] program = new String[2];
        Programs programs = Programs.getInstance();
        program[0] = programs.getChannel(prog);
        program[1] = programs.getStartTime(prog);
        if (m_schedule.contains(program)) {
            m_schedule.remove(program);
        } else {
            m_schedule.add(program);
        }
    }
