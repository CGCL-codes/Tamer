    public synchronized void saveGameResult(GameResult result) {
        if (!initialized && persistent) {
            load();
        }
        int teamCount = result.getTeamCount();
        if (teamCount == 1) {
            return;
        }
        Score score1 = null;
        long previousRank1 = 0;
        long previousScore1 = 0;
        Collection<GamePlayer> winners = result.getPlayersAtRank(1);
        GamePlayer winner = winners.iterator().next();
        if (winner.isWinner()) {
            String name = winner.getTeamName() == null ? winner.getName() : winner.getTeamName();
            int type = winner.getTeamName() == null ? Score.TYPE_PLAYER : Score.TYPE_TEAM;
            score1 = getScore(name, type);
            previousRank1 = scores.indexOf(score1) + 1;
            previousRank1 = previousRank1 == 0 ? scores.size() + 1 : previousRank1;
            if (score1 == null) {
                score1 = new Score();
                score1.setName(name);
                score1.setType(type);
                scores.add(score1);
            }
            previousScore1 = score1.getScore();
            int points = teamCount >= 3 ? 3 : 2;
            score1.setScore(score1.getScore() + points);
        }
        Score score2 = null;
        long previousRank2 = 0;
        long previousScore2 = 0;
        Collection<GamePlayer> seconds = result.getPlayersAtRank(2);
        GamePlayer second = seconds.iterator().next();
        if (teamCount >= 5) {
            String name = second.getTeamName() == null ? second.getName() : second.getTeamName();
            int type = second.getTeamName() == null ? Score.TYPE_PLAYER : Score.TYPE_TEAM;
            score2 = getScore(name, type);
            previousRank2 = scores.indexOf(score1) + 1;
            previousRank2 = previousRank2 == 0 ? scores.size() + 1 : previousRank2;
            if (score2 == null) {
                score2 = new Score();
                score2.setName(name);
                score2.setType(type);
                scores.add(score2);
            }
            previousScore2 = score2.getScore();
            score2.setScore(score2.getScore() + 1);
        }
        Collections.sort(scores, new ScoreComparator());
        Channel channel = result.getChannel();
        if (channel != null && config.getBoolean("display.score", false)) {
            channel.send(getGainMessage(score1, previousScore1, previousRank1));
            if (score2 != null) {
                channel.send(getGainMessage(score2, previousScore2, previousRank2));
            }
        }
        if (persistent) {
            save();
        }
    }
