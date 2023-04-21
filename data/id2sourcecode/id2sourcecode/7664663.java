    @Override
    public void sendMessage(String target, String message) {
        if (message.startsWith("/")) {
            doCommand(message.substring(1));
        } else {
            if (target == null || target.equals(Connection.CONSOLE_ROOM)) {
                return;
            }
            message = Message.quicklinkToLink(message);
            RoomManager.enQueue(new Message(this.getConnection(), message, this.getUserBot(), getChannel(target), Message.MSG));
            super.sendMessage(target, message);
        }
    }
