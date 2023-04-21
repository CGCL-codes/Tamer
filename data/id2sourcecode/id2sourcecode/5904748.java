    public void newGame(int points) {
        if (points > maxPoints) {
            maxPoints = points;
        }
        countOfGames++;
        averagePoints = (averagePoints + points) / 2;
    }
