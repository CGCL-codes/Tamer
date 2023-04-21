    public TestNoGuiServerApp(int serverPort, PrintWriter writer, TestMessageType msgType, TestBrokerType sockType, boolean isBlocking) {
        switch(sockType) {
            case PERS:
                _type = MessageUtils.SocketType.PERSISTANT;
                break;
            case TRANS:
                _type = MessageUtils.SocketType.TRANSIENT;
                break;
            default:
                AssertUtils.throwIllegalArgument(this, "Bad socket type. type=" + sockType);
        }
        _messagePrinter = new MessageIOPrinter(APP_NAME, writer);
        switch(msgType) {
            case JAVA:
                _thread = newJavaThread(serverPort, _messagePrinter);
                break;
            case JSON:
                _thread = newJsonThread(serverPort, _messagePrinter);
                break;
        }
        _threadPrinter = new NetThreadPrinter(APP_NAME, writer);
        _thread.addNetThreadListener(_threadPrinter);
        if (isBlocking) {
            _thread.run();
        } else {
            _thread.start();
        }
    }
