    public static void main(String[] args) {
        boolean cond = true;
        EnyarokClient my = EnyarokClient.get();
        try {
            my.connect("localhost", 32160);
            if (args.length == 3) {
                my.createAccount(args[0], args[1], args[2]);
                System.out.println("account creation");
            }
            my.login(args[0], args[1]);
            if (my.getAvailableCharacters().length == 0) {
                RPObject character = new RPObject();
                my.createCharacter(args[0], character);
                System.out.println("character creation");
            }
            my.chooseCharacter(args[0]);
        } catch (Exception e) {
            e.printStackTrace();
            cond = false;
        }
        int i = 0;
        while (cond) {
            ++i;
            my.loop(0);
            if (i % 100 == 50) {
                my.sendMessage("test" + i);
            }
            EventLine line = my.popEventLine();
            while (line != null) {
                System.out.println("[" + line.getHeader() + "]" + line.getText());
                line = my.popEventLine();
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                cond = false;
            }
        }
    }
