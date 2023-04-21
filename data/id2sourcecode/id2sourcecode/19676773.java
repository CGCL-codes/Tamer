    public static Classroom create(String name) {
        ChannelManager cman = AppContext.getChannelManager();
        Classroom classroom;
        try {
            cman.getChannel(name);
            return null;
        } catch (NameNotBoundException ex) {
            classroom = new Classroom();
            classroom.setBinding(name);
            Channel chnl = cman.createChannel(name, classroom, Delivery.RELIABLE);
            classroom.setChannel(chnl);
            return classroom;
        }
    }
