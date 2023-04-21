    public void update(Observable o, Object arg) {
        if (arg instanceof Message) {
            Message message = (Message) arg;
            StringBuffer buffer = new StringBuffer();
            buffer.append(message.getDate().toString());
            buffer.append(" ");
            buffer.append("[" + message.getChannel() + "]");
            buffer.append(" ");
            buffer.append("\"" + message.getAvatar() + "\"");
            buffer.append(" ");
            buffer.append(message.getContent());
            System.out.println(buffer.toString());
        } else System.out.println(arg.toString());
    }
