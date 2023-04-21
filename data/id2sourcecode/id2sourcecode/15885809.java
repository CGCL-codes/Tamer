    @BeforeTest
    void setup() {
        conMan = new ConnectionManager(profile) {
        };
        connection = new Connection(null, null, null) {

            @Override
            Channel getChannel(String name) {
                return new Channel(name, session);
            }
        };
        session = new Session(null) {

            @Override
            Connection getConnection() {
                return connection;
            }
        };
    }
