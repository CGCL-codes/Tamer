    protected void menu() throws IOException {
        debug.write("simulator started");
        keepRunning = true;
        String option = "1";
        int optionInt;
        while (keepRunning) {
            System.out.println();
            System.out.println("- 1 start simulation");
            System.out.println("- 2 stop simulation");
            System.out.println("- 3 list clients");
            System.out.println("- 4 send message");
            System.out.println("- 5 list messages");
            System.out.println("- 6 reload users file");
            System.out.println("- 7 log to screen " + (displayInfo ? "off" : "on"));
            System.out.println("- 0 exit");
            System.out.print("> ");
            optionInt = -1;
            try {
                option = keyboard.readLine();
                optionInt = Integer.parseInt(option);
            } catch (Exception e) {
                debug.write("exception reading keyboard " + e);
                optionInt = -1;
            }
            switch(optionInt) {
                case 1:
                    start();
                    break;
                case 2:
                    stop();
                    break;
                case 3:
                    listClients();
                    break;
                case 4:
                    sendMessage();
                    break;
                case 5:
                    messageList();
                    break;
                case 6:
                    reloadUsers();
                    break;
                case 7:
                    logToScreen();
                    break;
                case 0:
                    exit();
                    break;
                case -1:
                    break;
                default:
                    System.out.println("Invalid option. Choose between 0 and 6.");
                    break;
            }
        }
        System.out.println("Exiting simulator.");
        debug.write("simulator exited.");
    }
