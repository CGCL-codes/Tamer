    public double predictRating(int userID, short movieID) {
        DataLookup ld = DataLookup.getInstance();
        int userIndex = DataInfo.getUserIndex(userID);
        double userA = ld.getUserAverage(userIndex);
        double movieA = ld.getMovieAverage(movieID);
        double userV = ld.getUserStdDev(userIndex);
        double movieV = ld.getMovieStdDev(movieID);
        DataInfo.checkInvalidDouble("movieV", movieV);
        DataInfo.checkInvalidDouble("userV", userV);
        double userR = 0.0;
        if (userV > 0) userR = 1 / userV;
        double movieR = 0.0;
        if (movieR > 0) movieR = 1 / movieV;
        double sumR = userR + movieR;
        if (sumR == 0) {
            return (userA + movieA) / 2;
        }
        DataInfo.checkInvalidDouble("sumR", sumR);
        double avg = (userA + movieA) / 2;
        return avg;
    }
