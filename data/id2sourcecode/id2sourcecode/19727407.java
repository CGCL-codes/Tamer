    public ActiveReaderThread(InputStream in, int type, RuntimeAdapter runner, Writer outwriter) {
        this.in = in;
        this.type = type;
        this.outwriter = outwriter;
        this.runner = runner;
        this.listener = runner.getRuntimeListener();
    }
