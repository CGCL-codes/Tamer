    public USARSimServerImpl() throws RemoteException {
        super();
        boolean gotSocket = false;
        for (int i = 0; i < 10; i++) {
            try {
                socket = new Socket(host, port);
                socket.setSoTimeout(500);
                gotSocket = true;
                break;
            } catch (IOException ioe) {
                System.err.println(prg + ": Error opening socket: " + ioe);
                System.exit(-1);
            }
        }
        if (!gotSocket) {
            System.err.println(prg + ": Error opening socket");
            System.exit(-1);
        }
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = socket.getOutputStream();
        } catch (IOException ioe) {
            System.err.println(prg + ": Error creating reader/writer: " + ioe);
            System.exit(-1);
        }
        if (reddy) {
            name = "CRAMER";
        } else {
            name = "Pioneer";
        }
        u = new Updater();
        u.start();
        getStartPoses();
        if (autoPose) {
            if (startPoses.isEmpty()) {
                System.out.println("Failed to Retrieve StartPose Data");
            } else {
                if (startPoses.containsKey(desiredPose)) {
                    double[] poseLoc = startPoses.get(desiredPose);
                    initx = poseLoc[0];
                    inity = poseLoc[1];
                    initz = poseLoc[2];
                    initt = poseLoc[3];
                } else {
                    System.out.println(desiredPose + " is Not a Possible Pose");
                }
            }
        }
        sendMsg("INIT {ClassName USARBot." + ((reddy) ? "Cramer" : "StereoP2AT") + "} {Name " + name + "} {Location " + initx + "," + -(inity) + "," + initz + "} {Rotation 0.0,0.0," + -initt + "}");
        sendMsg("GETGEO {Type Robot}");
        sendMsg("GETCONF {Type Robot}");
        CMServer = new ContMotionServer();
        initialize();
        if (runTest) test();
        if (doDemo) {
            smile();
            Sleep(5000);
            try {
                moveRightArm(90, 90, 90);
            } catch (RemoteException re) {
            }
            Sleep(1500);
            WaveSpeak("com/reddy/wav/kramer1.wav");
            try {
                moveHead(80, 105);
                moveLeftArm(5, 10, 90);
                moveRightArm(5, 5, 5);
                moveMouth(45, 45, 45, 45);
                moveEyeBrows(55, 55);
            } catch (RemoteException re) {
            }
            Sleep(1500);
            WaveSpeak("com/reddy/wav/kramer2.wav");
            try {
                moveHead(90, 90);
                moveLeftArm(90, 10, 90);
                moveRightArm(5, 5, 5);
            } catch (RemoteException re) {
            }
            scowl();
            Sleep(1500);
            WaveSpeak("com/reddy/wav/kramer3.wav");
            Sleep(5000);
            System.exit(0);
        }
        if (doSmile) smile();
        if (doFrown) scowl();
        if (doHRIvideo) {
            try {
                Sleep(7000);
                smile();
                Sleep(1000);
                WaveSpeak("com/reddy/wav/hri08video01.wav");
                Sleep(6000);
                moveMouth(45, 45, 45, 45);
                moveHead(70, 90);
                Sleep(2000);
                moveHead(90, 90);
                Sleep(2000);
                moveEyeBrows(15, 15, 5);
                Sleep(8000);
                moveEyeBrows(30, 30);
                WaveSpeak("com/reddy/wav/hri08video02.wav");
                Sleep(4000);
                moveEyeBrows(45, 45);
                WaveSpeak("com/reddy/wav/hri08video03.wav");
                moveLeftArm(15, 5, 90);
                Sleep(1500);
                WaveSpeak("com/reddy/wav/hri08video04.wav");
                moveLeftArm(15, 30, 5);
                Sleep(1500);
                scowl();
                WaveSpeak("com/reddy/wav/hri08video05.wav");
                moveLeftArm(5, 5, 5);
                Sleep(1500);
                WaveSpeak("com/reddy/wav/hri08video06.wav");
                Sleep(500);
                WaveSpeak("com/reddy/wav/hri08video07.wav");
                Sleep(500);
                WaveSpeak("com/reddy/wav/hri08video08.wav");
            } catch (RemoteException re) {
            }
        }
    }
