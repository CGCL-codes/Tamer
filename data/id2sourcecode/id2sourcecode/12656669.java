    public OTPProxyRequest(String userName, JRadiusRealm realm, Socket socket, BufferedReader reader, BufferedWriter writer) throws OTPProxyException {
        this.userName = userName;
        this.otpName = RadiusRandom.getRandomString(16);
        this.otpPassword = RadiusRandom.getRandomString(16);
        this.socket = socket;
        this.reader = reader;
        this.writer = writer;
        this.radiusRealm = realm;
        try {
            radiusClient = new RadiusClient(InetAddress.getByName(this.radiusRealm.getServer()), this.radiusRealm.getSharedSecret());
        } catch (UnknownHostException e) {
            throw new OTPProxyException(e.getMessage());
        }
    }
