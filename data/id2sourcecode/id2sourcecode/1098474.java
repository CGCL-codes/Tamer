    public static void main(String[] args) {
        String logFileName = "./logs/megameklab.log";
        Locale.setDefault(Locale.US);
        new File("./data/mechfiles/units.cache").delete();
        boolean logs = true;
        boolean vehicle = false;
        boolean battlearmor = false;
        for (int pos = 0; pos < args.length; pos++) {
            if (args[pos].equalsIgnoreCase("-vehicle")) {
                vehicle = true;
            }
            if (args[pos].equalsIgnoreCase("-battlearmor")) {
                battlearmor = true;
            }
            if (args[pos].equalsIgnoreCase("-nolog")) {
                logs = false;
            }
        }
        if (logs) {
            try {
                if (!new File("./logs/").exists()) {
                    new File("./logs/").mkdir();
                }
                PrintStream ps = new PrintStream(new BufferedOutputStream(new FileOutputStream(logFileName), 64));
                System.setOut(ps);
                System.setErr(ps);
            } catch (Exception ex) {
                System.err.println("Unable to redirect output");
            }
        }
        if (vehicle) {
            Runtime runtime = Runtime.getRuntime();
            System.out.println("Memory Allocated [" + (runtime.maxMemory() / 1000) + "]");
            if (runtime.maxMemory() < 200000000) {
                try {
                    String[] call = { "java", "-Xmx256m", "-splash:data/images/splash/megameklabsplashvehicle.jpg", "-jar", "MegaMekLab.jar", "-vehicle" };
                    if (!logs) {
                        call = new String[] { "java", "-Xmx256m", "-splash:data/images/splash/megameklabsplashvehicle.jpg", "-jar", "MegaMekLab.jar", "-vehicle", "-nolog" };
                    }
                    runtime.exec(call);
                    System.exit(0);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            new megameklab.com.ui.Vehicle.MainUI();
        } else if (battlearmor) {
            Runtime runtime = Runtime.getRuntime();
            System.out.println("Memory Allocated [" + (runtime.maxMemory() / 1000) + "]");
            if (runtime.maxMemory() < 200000000) {
                try {
                    String[] call = { "java", "-Xmx256m", "-splash:data/images/splash/megameklabsplashbattlearmor.jpg", "-jar", "MegaMekLab.jar", "-battlearmor" };
                    if (!logs) {
                        call = new String[] { "java", "-Xmx256m", "-splash:data/images/splash/megameklabsplashbattlearmor.jpg", "-jar", "MegaMekLab.jar", "-battlearmor", "-nolog" };
                    }
                    runtime.exec(call);
                    System.exit(0);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            new megameklab.com.ui.BattleArmor.MainUI();
        } else {
            Runtime runtime = Runtime.getRuntime();
            System.out.println("Memory Allocated [" + (runtime.maxMemory() / 1000) + "]");
            if (runtime.maxMemory() < 200000000) {
                try {
                    String[] call = { "java", "-Xmx256m", "-jar", "MegaMekLab.jar" };
                    if (!logs) {
                        call = new String[] { "java", "-Xmx256m", "-jar", "MegaMekLab.jar", "-nolog" };
                    }
                    runtime.exec(call);
                    System.exit(0);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            new MainUI();
        }
    }
