    public static void main(String[] args) {
        PlayerClient robot = null;
        Position2DInterface posi = null;
        SonarInterface soni = null;
        try {
            robot = new PlayerClient("localhost", 6665);
            posi = robot.requestInterfacePosition2D(0, PlayerConstants.PLAYER_OPEN_MODE);
            soni = robot.requestInterfaceSonar(0, PlayerConstants.PLAYER_OPEN_MODE);
        } catch (PlayerException e) {
            System.err.println("SpaceWandererExample: > Error connecting to Player: ");
            System.err.println("    [ " + e.toString() + " ]");
            System.exit(1);
        }
        robot.runThreaded(-1, -1);
        while (true) {
            while (!soni.isDataReady()) ;
            sonarValues = soni.getData().getRanges();
            for (int i = 0; i < soni.getData().getRanges_count(); i++) if (sonarValues[i] < SONAR_MIN_VALUE) sonarValues[i] = SONAR_MIN_VALUE; else if (sonarValues[i] > SONAR_MAX_VALUE) sonarValues[i] = SONAR_MAX_VALUE;
            System.out.println(decodeSonars(soni));
            leftSide = (sonarValues[1] + sonarValues[2]) / 2;
            rightSide = (sonarValues[5] + sonarValues[6]) / 2;
            leftSide = leftSide / 10;
            rightSide = rightSide / 10;
            xspeed = (leftSide + rightSide) / 2;
            yawspeed = (float) ((leftSide - rightSide) * (180 / Math.PI) / WHEEL_DIAMETER);
            try {
                Thread.sleep(100);
            } catch (Exception e) {
            }
            if (((sonarValues[1] > SONAR_THRESHOLD) && (sonarValues[2] > SONAR_THRESHOLD) && (sonarValues[3] > SONAR_THRESHOLD)) || ((sonarValues[4] > SONAR_THRESHOLD) && (sonarValues[5] > SONAR_THRESHOLD) && (sonarValues[6] > SONAR_THRESHOLD))) posi.setSpeed(xspeed, yawspeed); else if (sonarValues[0] < sonarValues[7]) posi.setSpeed(0, -DEF_YAW_SPEED); else posi.setSpeed(0, DEF_YAW_SPEED);
        }
    }
