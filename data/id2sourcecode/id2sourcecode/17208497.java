    private void sendMessage(Link link, RouterMessage rm) {
        Channel c = link.getChannel();
        if (c == null) {
            receiveMessage(link, rm);
        } else {
            c.write(rm);
        }
    }
