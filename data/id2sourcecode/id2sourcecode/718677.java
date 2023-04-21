        public void run() {
            Scanner scanner = new Scanner(System.in);
            String input = "";
            while (true) {
                if ((input = scanner.nextLine()) != null) {
                    try {
                        server.writeline("PRIVMSG " + server.getChannel() + " :" + input + "\r\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
