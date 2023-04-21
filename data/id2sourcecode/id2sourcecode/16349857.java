    public String toString() {
        if (goalsTeamOne == -1) {
            return team1.getName() + " - " + team2.getName() + ", " + this.getNiceTime() + ", " + getChannel() + ". ";
        } else {
            return team1.getName() + " - " + team2.getName() + " (" + goalsTeamOne + "-" + goalsTeamTwo + "), " + this.getNiceTime() + ", " + getChannel() + ". ";
        }
    }
