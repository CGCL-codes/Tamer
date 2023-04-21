    public List<Program> getUpcomingPrograms() {
        List<Program> onAirPrograms = getOnAirPrograms();
        List<Program> upComingPrograms = new ArrayList<Program>();
        for (Program p : scheduleList) {
            if (p.getState() == ProgramState.UNKNOWN) continue;
            for (ProgramInterface onAirProgram : onAirPrograms) {
                if (onAirProgram.getChannel() == p.getChannel() && onAirProgram.getStopDate().compareTo(p.getStartDate()) == 0) {
                    upComingPrograms.add(p);
                }
            }
        }
        return upComingPrograms;
    }
