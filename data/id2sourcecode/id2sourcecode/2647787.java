    public static void main(String[] args) {
        Client client = new Client();
        client.connect("localhost");
        if (client.login("brian", "notencrypted") == true) {
            System.out.println("0: " + client.addFriend("AmySmart", "justin"));
            System.out.println("1: " + client.upload("brian\\girls\\ec\\cute1a.jpg"));
            System.out.println("2: " + client.upload("brian\\girls\\ec\\source.jpg"));
            System.out.println("3: " + client.upload("brian\\girls\\ec\\cute2a.jpg"));
            System.out.println("4: " + client.upload("brian\\KristenKreuk\\kristen34.jpg"));
            System.out.println("5: " + client.delete("brian\\girls\\"));
            System.out.println("6: " + client.changePassword("password1"));
        }
        try {
            System.out.println("Sleeping...");
            Thread.sleep(1000);
        } catch (Exception e) {
            System.out.println("Can't sleep: " + e);
        }
        client.quit();
        client.connect("localhost");
        if (client.login("brian", "password1") == true) {
            System.out.println("7: " + client.download("stuart"));
            System.out.println("8: " + !client.download("stuart\\cars"));
            System.out.println("9: " + client.download("stuart\\Wallpapers"));
            System.out.println("10: " + client.download("stuart\\Wallpapers\\Elisha Cuthbert"));
            System.out.println("11: " + client.changePassword("notencrypted"));
            System.out.println("12: " + !client.addFriend("AmySmart", "stuart"));
            System.out.println("13: " + client.rmFriend("AmySmart", "justin"));
        } else {
            System.out.println("**Test 3 Failed**");
        }
        try {
            System.out.println("Sleeping...");
            Thread.sleep(1000);
        } catch (Exception e) {
            System.out.println("Can't sleep: " + e);
        }
        client.quit();
        client.connect("localhost");
        if (client.login("stuart", "noseethis") == true) {
            System.out.println("14: " + client.addFriend("cars", "brian"));
            System.out.println("15: " + client.changePassword("boogers"));
        } else {
            System.out.println("**Test 4 Failed**");
        }
        try {
            System.out.println("Sleeping...");
            Thread.sleep(1000);
        } catch (Exception e) {
            System.out.println("Can't sleep: " + e);
        }
        client.quit();
        client.connect("localhost");
        if (client.login("brian", "notencrypted") == true) {
            System.out.println("16: " + client.download("stuart\\cars"));
        } else {
            System.out.println("**Test 5 Failed**");
        }
        try {
            System.out.println("Sleeping...");
            Thread.sleep(1000);
        } catch (Exception e) {
            System.out.println("Can't sleep: " + e);
        }
        client.quit();
        client.connect("localhost");
        if (client.login("stuart", "boogers") == true) {
            System.out.println("17: " + client.rmFriend("cars", "brian"));
            System.out.println("18: " + client.changePassword("noseethis"));
        } else {
            System.out.println("**Test 6 Failed**");
        }
        try {
            System.out.println("Sleeping...");
            Thread.sleep(1000);
        } catch (Exception e) {
            System.out.println("Can't sleep: " + e);
        }
        client.quit();
    }
