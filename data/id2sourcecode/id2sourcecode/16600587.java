    void initialize() {
        if (!configured()) return;
        final Client local = bayeux.newClient("myapp.server.client");
        ListenerCallback callback = new ListenerCallback() {

            public void handle(Map<String, Object> m, IBListener l) throws Exception {
                HashMap<String, Object> n = (HashMap<String, Object>) m;
                if (m.containsKey("blipsystem.id")) {
                    EventPublisher ep = new EventPublisher();
                    ep.publish(n);
                    String currentZone = (String) m.get("zone.current");
                    System.out.println(currentZone);
                    Channel channel = bayeux.getChannel("/blip/zones/" + currentZone, true);
                    channel.publish(local, m, null);
                } else if (m.containsKey("mobileOS_id")) {
                    EventPublisher ep = new EventPublisher();
                    ep.publish(n);
                } else {
                    System.out.println("unknown event type");
                }
            }
        };
        try {
            ibBTListener = infobus.createListener(callback, "listenerBlip", infobus.createToken("blipsystem.id", "blipsystem.itu"));
            ibGPSListener = infobus.createListener(callback, "listenerGps", infobus.createToken("type", "device.GPSpos"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
