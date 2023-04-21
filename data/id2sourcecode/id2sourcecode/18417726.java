    protected boolean iterativeMethod(Sprite s1, Sprite s2, double lowerT, double higherT, double oldX1, double oldY1, double oldX2, double oldY2, double speedX1, double speedY1, double speedX2, double speedY2) {
        double workingT = (lowerT + higherT) / 2;
        double curX1, curY1, curX2, curY2;
        double maxSpeed = Math.max(Math.max(Math.abs(speedX1), Math.abs(speedY1)), Math.max(Math.abs(speedX2), Math.abs(speedY2)));
        while (true) {
            curX1 = oldX1 + workingT * speedX1;
            curY1 = oldY1 + workingT * speedY1;
            curX2 = oldX2 + workingT * speedX2;
            curY2 = oldY2 + workingT * speedY2;
            if (this.checkCollisionHelper(s1, s2, curX1, curY1, curX2, curY2, true)) {
                higherT = workingT;
                workingT = (workingT + lowerT) / 2;
                if ((higherT - lowerT) * maxSpeed < PreciseCollisionGroup.ITERATIVE_BAILOUT) {
                    System.err.println("Iterative failure-- too close");
                    break;
                }
            } else if (this.checkAdjacencyHelper(s1, s2, curX1, curY1, curX2, curY2, speedX1, speedY1, speedX2, speedY2, true)) {
                this.collisionX1 = Math.abs(curX1 - oldX1) > 2 * PreciseCollisionGroup.ADJACENCY_TOLERANCE ? curX1 : oldX1;
                this.collisionY1 = Math.abs(curY1 - oldY1) > 2 * PreciseCollisionGroup.ADJACENCY_TOLERANCE ? curY1 : oldY1;
                this.collisionX2 = Math.abs(curX2 - oldX2) > 2 * PreciseCollisionGroup.ADJACENCY_TOLERANCE ? curX2 : oldX2;
                this.collisionY2 = Math.abs(curY2 - oldY2) > 2 * PreciseCollisionGroup.ADJACENCY_TOLERANCE ? curY2 : oldY2;
                return true;
            } else {
                lowerT = workingT;
                workingT = (workingT + higherT) / 2;
                if ((higherT - lowerT) * maxSpeed < PreciseCollisionGroup.ITERATIVE_BAILOUT) {
                    break;
                }
            }
        }
        return false;
    }
