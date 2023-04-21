    public static void main(String[] args) {
        if (args.length != 6) {
            logError("Invalid args length: " + args.length);
            halt(JudgeReply.JUDGE_INTERNAL_ERROR);
        }
        GregorianCalendar gc = new GregorianCalendar();
        try {
            Scanner scanner = new Scanner(args[0]);
            port = scanner.nextInt();
            timeLimit = Integer.parseInt(args[1]);
            memoryLimit = Integer.parseInt(args[2]);
            outputLimit = Integer.parseInt(args[3]);
            uid = Integer.parseInt(args[4]);
            gid = Integer.parseInt(args[5]);
            socket = new Socket("127.0.0.1", port);
            out = new DataOutputStream(socket.getOutputStream());
            System.setIn(new BufferedInputStream(new FileInputStream("input")));
            System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream("p.out") {

                public void write(int b) throws IOException {
                    try {
                        super.write(b);
                    } catch (IOException e) {
                        if (e.getMessage().equals("File too large")) {
                            SandboxSecurityManager.targetThread = null;
                            halt(JudgeReply.OUTPUT_LIMIT_EXCEEDED);
                        }
                        throw e;
                    }
                }

                public void write(byte[] b, int off, int len) throws IOException {
                    try {
                        super.write(b, off, len);
                    } catch (IOException e) {
                        if (e.getMessage().equals("File too large")) {
                            SandboxSecurityManager.targetThread = null;
                            halt(JudgeReply.OUTPUT_LIMIT_EXCEEDED);
                        }
                        throw e;
                    }
                }
            })));
            System.setErr(new PrintStream(new BufferedOutputStream(new FileOutputStream("/dev/null"))));
        } catch (Exception e) {
            logError(printError(e));
            halt(JudgeReply.JUDGE_INTERNAL_ERROR);
            return;
        }
        System.gc();
        baseHeapMemoryConsumption = memoryBean.getHeapMemoryUsage().getUsed();
        targetThread.start();
        for (; ; ) {
            Thread.State state;
            ThreadInfo info = threadBean.getThreadInfo(targetThread.getId());
            if (info == null) {
                state = Thread.State.TERMINATED;
            } else {
                state = info.getThreadState();
            }
            if (state == Thread.State.RUNNABLE || state == Thread.State.NEW || state == Thread.State.TERMINATED) {
                updateConsumptions();
                try {
                    sendRunningMessage(timeConsumption, memoryConsumption);
                } catch (IOException e) {
                    halt(JudgeReply.JUDGE_INTERNAL_ERROR);
                }
                if (state == Thread.State.TERMINATED) {
                    break;
                }
            } else if (SandboxSecurityManager.targetThread != null) {
                logError("Invalid thread state " + state);
                halt(JudgeReply.RUNTIME_ERROR);
            }
            try {
                targetThread.join(1000);
            } catch (InterruptedException e) {
                Runtime.getRuntime().halt(0);
                break;
            }
        }
        closeSocket();
    }
