    @Override
    protected void setUp() throws Exception {
        super.setUp();
        playersFile = File.createTempFile("Players.", ".txt");
        scheduleFile = File.createTempFile("Games.", ".txt");
        teamsFile = File.createTempFile("Teams.", ".txt");
        FileUtils.copyFile(PLAYERS_FILE, playersFile);
        FileUtils.copyFile(SCHEDULE_FILE, scheduleFile);
        FileUtils.copyFile(TEAMS_FILE, teamsFile);
        playersFile.deleteOnExit();
        scheduleFile.deleteOnExit();
        teamsFile.deleteOnExit();
        service = new FileTournamentService();
        service.setPlayersFile(playersFile);
        service.setScheduleFile(scheduleFile);
        service.setTeamsFile(teamsFile);
        service2 = new FileTournamentService();
        service2.setPlayersFile(playersFile);
        service2.setScheduleFile(scheduleFile);
        service2.setTeamsFile(teamsFile);
        service3 = new FileTournamentService();
        service3.setPlayersFile(playersFile);
        service3.setScheduleFile(scheduleFile);
        service3.setTeamsFile(teamsFile);
        service4 = new FileTournamentService();
        service4.setPlayersFile(playersFile);
        service4.setScheduleFile(scheduleFile);
        service4.setTeamsFile(teamsFile);
    }
