    public boolean isCompetitionWon() {
        int neededWins = (bestOf + 1) / 2;
        if ((getHomeWins() >= neededWins) || (getAwayWins() >= neededWins)) {
            return true;
        } else {
            return false;
        }
    }
