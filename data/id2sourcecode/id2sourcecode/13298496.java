    public static boolean testDBConnection(String host, String port, String db, String user, String password) {
        try {
            mysqlDB = db.trim();
            mysqlhost = host.trim();
            mysqlPassword = password.trim();
            mysqlUser = user.trim();
            mysqlPort = port.trim();
            System.out.println("Server:" + host);
            System.out.println("Port:" + port);
            System.out.println("DB:" + db);
            System.out.println("User:" + user);
            System.out.println("Password:" + password);
            String readFile = readFile(APP_HOME + "/config/SampleTorque.properties");
            readFile = readFile.replace("#host#", host);
            readFile = readFile.replace("#db#", db);
            readFile = readFile.replace("#port#", port);
            readFile = readFile.replace("#user#", user);
            readFile = readFile.replace("#password#", password);
            writeFile(APP_HOME + "/config/Torque.properties", readFile);
            try {
                if (Torque.isInit()) {
                    Torque.shutdown();
                }
            } catch (TorqueException e) {
                e.printStackTrace();
            }
            return configureTorque();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
