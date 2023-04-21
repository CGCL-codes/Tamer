    public static void main(String[] args) {
        DBHelper db = null;
        try {
            db = new DBHelper();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Database connected");
        int dist = 0;
        int angle = 0;
        int seqn;
        int reading;
        while (true) {
            System.out.println("Current ANGLE = " + angle);
            System.out.print("Input DISTANCE: ");
            dist = Console.readInt();
            try {
                Console.cutOffLine();
            } catch (IOException e) {
                System.out.println("WARNING: Wrong input, try again");
                continue;
            }
            if (dist < 0) {
                break;
            }
            if (dist == 0) {
                System.out.print("Enter new ANGLE: ");
                angle = Console.readInt();
                Console.cutOffLine("Wrong input format");
                continue;
            }
            System.out.println("dist=" + dist + "    angle=" + angle);
            for (int i = 0; i < 10; i++) {
                seqn = i;
                reading = NXT.sonar();
                System.out.print(reading + " ");
                try {
                    db.writeSonar(dist, angle, seqn, reading);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            System.out.println();
            NXT.keepAlive();
        }
        NXT.close();
        System.out.println("NXT disconnected");
        try {
            db.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Dababase disconnected");
    }
