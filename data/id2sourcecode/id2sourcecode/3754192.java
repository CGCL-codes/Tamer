    public void activateOptions() {
        if (debug) {
            System.out.println("Activate options");
        }
        if (buffertype.equalsIgnoreCase("autopop")) {
            eventQueue = new Fifo(buffersize, Fifo.AUTOPOP);
        } else if (buffertype.equalsIgnoreCase("refuse")) {
            eventQueue = new Fifo(buffersize, Fifo.REFUSE);
        } else {
            eventQueue = new Fifo(buffersize, Fifo.AUTOPOP);
        }
        irc = doIrcBotInitialization();
        irc.setEventQueue(eventQueue);
        try {
            irc.connect();
            irc.login();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
