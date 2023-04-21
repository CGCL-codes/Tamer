    @Before
    public void setUp() throws Exception {
        TestPVRStateFactory factory = TestPVRStateFactory.getInstance();
        Station station;
        this.now = new Date();
        this.state = new PVRState();
        this.state.setLocator(new ResourceLocator());
        factory.buildChannels(this.state, this.now);
        station = this.state.getStation(ServiceType.TV, this.CHANNEL);
        this.epg = new Channel(this.state, station.getChannel(), this.CHANNEL);
        this.eventId = 5000;
    }
