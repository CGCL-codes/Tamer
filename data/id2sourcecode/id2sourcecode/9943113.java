    public static void test1() {
        final GenerateEvents ge = new GenerateEvents();
        ge.start();
        Container containerBroadcaster = new Container();
        Container containerListener = new Container();
        System.out.println("Registering Broadcaster");
        containerBroadcaster.registerBroadcast("MONITOR", new ChannelProgramAdaptor() {

            public void hook(Container container, Channel channel) {
                System.out.println("ChannelProgramAdaptor Hooking container for monitor1");
                ge.addContainer(container);
            }

            @Override
            public void unhook(Container container, Channel channel) {
                System.out.println("ChannelProgramAdaptor UnHooking container for monitor1");
                ge.removeContainer(container);
            }
        });
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Registering listener");
        ChannelProgram listenerChannelProgram = new ChannelProgramAdaptor() {

            @Override
            public void listen(Container container, Channel channel, BroadcastInfo info) {
                System.out.println("Receive Broadcasted from Channel " + channel.getChannelName());
            }
        };
        containerListener.registerListener("MONITOR", listenerChannelProgram);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("DeRegistering Listener");
        containerListener.deregisterListener("MONITOR", listenerChannelProgram);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Registering listener");
        containerListener.registerListener("MONITOR", listenerChannelProgram);
        final GenerateEvents2 ge2 = new GenerateEvents2();
        ge2.start();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("DeRegistering Listener");
        containerListener.deregisterListener("MONITOR", listenerChannelProgram);
        System.out.println("Registering listener");
        containerListener.registerListener("MONITOR2", listenerChannelProgram);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("STEP 10 : Registering Broadcaster");
        containerBroadcaster.registerBroadcast("MONITOR2", new ChannelProgramAdaptor() {

            public void hook(Container container, Channel channel) {
                System.out.println("ChannelProgramAdaptor Hooking container for monitor2");
                ge2.addContainer(container);
            }

            @Override
            public void unhook(Container container, Channel channel) {
                System.out.println("ChannelProgramAdaptor UnHooking container for monitor2");
                ge2.removeContainer(container);
            }
        });
    }
