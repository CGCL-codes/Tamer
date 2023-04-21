    public void run() throws Exception {
        final SKJConnection csp = sk.createConnection(props.getProperty("llc_host", "10.100.0.32"), Integer.parseInt(props.getProperty("llc_port", "-1")), props.getProperty("rllc_host", ""), Integer.parseInt(props.getProperty("rllc_port", "-1")), Integer.parseInt(props.getProperty("sk_app_id", "100")), new MySKConnectionListener());
        csp.pushListener(new MyEventListener(csp));
        int ConnID = csp.connect();
        logger.info("Created connection ID: " + ConnID);
        class MyChanGroupListener implements SKJEventListener {

            private MyApp app;

            public MyChanGroupListener(MyApp ma) {
                app = ma;
            }

            public boolean onEvent(SKJMessage msg) {
                logger.fine("Got the ACK for channel groups");
                SKJQueryAllChannelGroupsAck ack = (SKJQueryAllChannelGroupsAck) msg;
                String[] groups = ack.getChannelGroups();
                for (int i = 0; i < groups.length; i++) {
                    logger.finest("Channel Groups on switch: " + i + " = '" + groups[i] + "'");
                }
                app.setChannelGroups(groups);
                return true;
            }
        }
        ;
        csp.getAllChannelGroups(new MyChanGroupListener(this));
        logger.fine("Waiting for app groups response");
        this.waitForChannelGroups();
        logger.fine("Got app groups response");
        for (int i = 0; i < groups.length; i++) {
            logger.finest("Channel Groups on switch: " + i + " = '" + groups[i] + "'");
        }
        groups = csp.getAllUserChannelGroups(groups);
        for (int i = 0; i < groups.length; i++) {
            logger.finest("USER DEFINED Channel Groups on switch: " + i + " = '" + groups[i] + "'");
        }
        csp.setAppPoolListener("TEST_RAP", new SKJAppPoolStatusListener() {

            public void onBecomePrimary(String appPool, int appID) {
                logger.info("App " + appID + " in pool " + appPool + " has become Primary.");
            }

            public void onBecomeSecondary(String appPool, int appID) {
                logger.info("App " + appID + " in pool " + appPool + " has become Secondary.");
            }

            public void onMonitor(String appPool, int appID) {
                logger.info("App " + appID + " in pool " + appPool + " has become Monitor.");
            }

            public void onNoPrimary(String appPool, int appID) {
                logger.info("App " + appID + " in pool " + appPool + " has no primary.");
            }

            public void onRemoved(String appPool, int appID) {
                logger.info("App " + appID + " in pool " + appPool + " was removed.");
            }
        });
        class MyClass implements SKJEventListener {

            public boolean onEvent(SKJMessage msg) {
                if (msg instanceof SK_RegisterAsRedundantAppAck) {
                    SK_RegisterAsRedundantAppAck ack = (SK_RegisterAsRedundantAppAck) msg;
                    logger.info("SK_RegisterAsRedundantAppAck => status " + ack.getStatus() + ", switchover:" + ack.getInitiateSwitchover());
                    return true;
                }
                return false;
            }
        }
        ;
        final MyClass h = new MyClass();
        csp.joinAppPool("TEST_RAP", pri, null, h);
        csp.pushListener("TRITON_0", new SKJChanGroupEventListener() {

            public boolean onEvent(SKJMessage m) {
                return false;
            }

            public boolean onEvent(String g, SKJMessage m) {
                logger.severe("CHANGROUP LISTENER GOT: " + m.toString());
                return false;
            }
        });
        csp.requestIdleChannel("TRITON_0", new SKJEventListener() {

            public boolean onEvent(SKJMessage msg) {
                if (msg instanceof SK_RequestChannelAck) {
                    SK_RequestChannelAck ack = (SK_RequestChannelAck) msg;
                    if (ack.getSKStatus() == SKConstants.OK) {
                        logger.info("Got channel " + ack.getSpan() + ":" + ack.getChannel());
                        byte COLLECT_FIXED_NO_OF_DIGITS = 0x01;
                        byte COLLECT_MAX_DIGITS = 1;
                        byte COLLECT_NUM_TERM_CHARS = 0;
                        byte COLLECT_CONFIG_BITS = 0x1E;
                        int COLLECT_INTER_DIGIT_TIMER = 5000;
                        int COLLECT_FIRST_DIGIT_TIMER = 5000;
                        int COLLECT_COMPLETION_TIMER = 6000;
                        int COLLECT_MIN_RECEIVE_DIGIT_DURATION = 5;
                        byte COLLECT_NUM_DIGIT_STRINGS = 1;
                        int COLLECT_RESUME_DIGIT_CLTN_TIMER = 0;
                        int DSP_SERVICE_TYPE_DTMF_RECEIVER = (byte) 0x01;
                        int serviceType = DSP_SERVICE_TYPE_DTMF_RECEIVER;
                        XL_CollectDigitString cd = new XL_CollectDigitString();
                        cd.setNodeID(0xff);
                        cd.setSpan(ack.getSpan());
                        cd.setChannel(ack.getChannel());
                        cd.setMode(COLLECT_FIXED_NO_OF_DIGITS);
                        cd.setMaxDigits(COLLECT_MAX_DIGITS);
                        cd.setNumTermChars(COLLECT_NUM_TERM_CHARS);
                        cd.setConfigBits(COLLECT_CONFIG_BITS);
                        cd.setInterDigitTimer(COLLECT_INTER_DIGIT_TIMER);
                        cd.setFirstDigitTimer(COLLECT_FIRST_DIGIT_TIMER);
                        cd.setCompletionTimer(COLLECT_COMPLETION_TIMER);
                        cd.setMinReceiveDigitDuration(COLLECT_MIN_RECEIVE_DIGIT_DURATION);
                        cd.setAddressSignallingType(serviceType);
                        cd.setNumDigitStrings(COLLECT_NUM_DIGIT_STRINGS);
                        cd.setResumeDigitCltnTimer(COLLECT_RESUME_DIGIT_CLTN_TIMER);
                        class CollectDigitStringAckListener implements SKJEventListener {

                            public boolean onEvent(SKJMessage skjmessage) {
                                logger.log(Level.ALL, "entering");
                                boolean isProcessed = false;
                                if (skjmessage instanceof XL_CollectDigitStringAck) {
                                    isProcessed = true;
                                    System.out.println("Got the ACK!");
                                }
                                logger.log(Level.ALL, "exiting");
                                return isProcessed;
                            }
                        }
                        CollectDigitStringAckListener msgListener = new CollectDigitStringAckListener();
                        csp.sendMessage(cd, msgListener);
                        return true;
                    } else {
                        logger.info("Request channel got status:" + SKJava.statusText(ack.getSKStatus()));
                    }
                }
                return false;
            }
        });
        System.out.println("Joined group 'Testing' with status " + SKJava.statusText(csp.joinGroup("Testing")));
        SK_InterAppMsg iam = new SK_InterAppMsg();
        iam.setAppGroupTarget("Testing");
        iam.setTag1(0);
        iam.setDataSize(0);
        iam.setType(SKConstants.SK_IAM_BROADCAST);
        System.out.println("Sent a broadcast to  'Testing' with status " + SKJava.statusText(csp.sendMessage(iam, null)));
        new Timer().schedule(new TimerTask() {

            public void run() {
                System.out.println("Sent a reselction request: Status:" + SKJava.statusText(csp.requestAppPoolPrimaryReselection("TEST_RAP", algo, null)));
            }
        }, 15000);
        sk.waitForCompletion();
    }
