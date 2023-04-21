    private boolean sendMessage(String _message, ChWYServerThread _destThread) {
        String answer = "";
        int status = 0;
        int tries = 0;
        boolean messageSent = false;
        try {
            System.out.print("Writing message to destination user");
            _destThread.outputStream.writeUTF(ChWYConfig.MESSAGE_ORIG + userName + ";" + ChWYConfig.MESSAGE_STRING + _message);
            System.out.print("Message sent");
        } catch (IOException e) {
            org.digitall.lib.components.Advisor.messageBox("Error al enviar mensaje", "Error");
            e.printStackTrace();
        }
        return messageSent;
    }
