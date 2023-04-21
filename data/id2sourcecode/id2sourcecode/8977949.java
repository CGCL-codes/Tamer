    public void checkForMail() {
        try {
            POP3Client client = new POP3Client();
            client.connect("10.0.0.6", 110);
            if (client.login("name", "password")) {
                POP3MessageInfo[] info = client.listMessages();
                Reader reader = client.retrieveMessageTop(info[0].number, 1);
                BufferedReader bufferedReader = new BufferedReader(reader);
                String line = bufferedReader.readLine();
                StringBuffer stringBuffer = new StringBuffer();
                while (line != null) {
                    stringBuffer.append(line + "\n\r");
                    line = bufferedReader.readLine();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
