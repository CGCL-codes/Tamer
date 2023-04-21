    public static void main(String[] args) throws Exception {
        try {
            for (int i = 0; i < args.length; i++) {
                if (args[i].startsWith("-h")) {
                    printHelp();
                    System.exit(0);
                }
            }
            for (int i = 0; i < args.length; i++) {
                if (args[i].startsWith("-v")) {
                    verbose = true;
                    break;
                }
            }
            for (int i = 0; i < args.length; i++) {
                if (args[i].startsWith("if=")) {
                    sourceName = args[i].substring("if=".length());
                } else if (args[i].startsWith("of=")) {
                    destinationName = args[i].substring("of=".length());
                } else if (args[i].startsWith("bs=")) {
                    String bSParam = args[i].substring("bs=".length());
                    int factor = 1;
                    try {
                        if (bSParam.endsWith("k") || bSParam.endsWith("K")) {
                            factor = (int) KILO;
                            bSParam = bSParam.substring(0, bSParam.length() - 1);
                        } else if (bSParam.endsWith("m") || bSParam.endsWith("M")) {
                            factor = (int) MEGA;
                            bSParam = bSParam.substring(0, bSParam.length() - 1);
                        }
                        BUFF_SIZE = Integer.parseInt(bSParam) * factor;
                    } catch (Throwable t) {
                        if (verbose) {
                            System.err.println("Cannot parse bsParam " + args[i] + " Cause: ");
                            t.printStackTrace();
                        } else {
                            System.err.println("Cannot parse bs param: " + args[i] + " . Try to run DDCopy with -v for further details");
                        }
                        printHelp();
                        System.err.flush();
                        System.out.flush();
                        System.exit(1);
                    }
                } else if (args[i].startsWith("bn=")) {
                    try {
                        BUFF_NO = Integer.parseInt(args[i].substring("bn=".length()));
                    } catch (Throwable t) {
                        BUFF_NO = 1;
                        if (verbose) {
                            System.err.println("Cannot parse bn param " + args[i] + " Cause: ");
                            t.printStackTrace();
                        } else {
                            System.err.println("Cannot parse bn param " + args[i] + ". Will use the default value: " + BUFF_NO);
                        }
                    }
                } else if (args[i].startsWith("count=")) {
                    try {
                        COUNT = Integer.parseInt(args[i].substring("count=".length()));
                    } catch (Throwable t) {
                        COUNT = -1;
                        if (verbose) {
                            System.err.println("Cannot parse count param " + args[i] + " Cause: ");
                            t.printStackTrace();
                        } else {
                            System.err.println("Cannot parse count param " + args[i] + ". Will use the default value: " + COUNT);
                        }
                    }
                } else if (args[i].startsWith("statsdelay=")) {
                    try {
                        delay = Long.parseLong(args[i].substring("count=".length())) * 1000L;
                    } catch (Throwable t) {
                        delay = 2 * 1000;
                        if (verbose) {
                            System.err.println("Cannot parse statsdelay param " + args[i] + " Cause: ");
                            t.printStackTrace();
                        } else {
                            System.err.println("Cannot parse statsdelay param " + args[i] + ". Will use the default value: " + delay / 1000 + " seconds");
                        }
                    }
                } else if (args[i].startsWith("flags=")) {
                    final String wFlag = args[i].substring("flags=".length());
                    if (wFlag.equalsIgnoreCase("NOSYNC")) {
                        wrFlags = "rw";
                    } else if (wFlag.equalsIgnoreCase("SYNC")) {
                        wrFlags = "rws";
                    } else if (wFlag.equalsIgnoreCase("DSYNC")) {
                        wrFlags = "rwd";
                    }
                } else if (args[i].startsWith("rformat=")) {
                    final String rFlag = args[i].substring("rformat=".length());
                    if (rFlag.equalsIgnoreCase("K")) {
                        reportingFactor = KILO;
                    } else if (rFlag.equalsIgnoreCase("M")) {
                        reportingFactor = MEGA;
                    } else if (rFlag.equalsIgnoreCase("G")) {
                        reportingFactor = GIGA;
                    } else if (rFlag.equalsIgnoreCase("T")) {
                        reportingFactor = TERA;
                    } else if (rFlag.equalsIgnoreCase("P")) {
                        reportingFactor = PETA;
                    }
                }
            }
            if (sourceName == null || sourceName.trim().length() == 0) {
                System.out.println("\n No source specified ( if=<SourceFile> parameter ). Use -h for help.\n");
                System.exit(1);
            }
            if (destinationName == null || destinationName.trim().length() == 0) {
                System.out.println("\n No destination specified ( 'of=<DestinationFile>' parameter ). Use -h for help.\n");
                System.exit(1);
            }
            if (verbose) {
                StringBuilder sb = new StringBuilder();
                sb.append("Source: ").append(sourceName);
                sb.append(" Destination: ").append(destinationName);
                sb.append("");
            }
            final FileChannel sourceChannel = new RandomAccessFile(sourceName, "r").getChannel();
            final FileChannel destinationChannel = new RandomAccessFile(destinationName, wrFlags).getChannel();
            ByteBuffer[] bbuff = new ByteBuffer[BUFF_NO];
            for (int i = 0; i < BUFF_NO; i++) {
                try {
                    bbuff[i] = ByteBuffer.allocateDirect(BUFF_SIZE);
                } catch (OutOfMemoryError oomError) {
                    System.err.println("ByteBuffer reached max limit. The copy may be slow. You may consider to increase to -XX:MaxDirectMemorySize=256m, or decrease the buffer number (bn) parameter");
                    System.err.flush();
                    System.exit(1);
                }
            }
            if (delay > 0) {
                reportingThread = new ReportingThread();
                reportingThread.start();
            }
            Runtime.getRuntime().addShutdownHook(new ShutdownHook());
            long count = 0;
            START_TIME = System.currentTimeMillis();
            for (int j = 0; (COUNT > 0) ? j < COUNT : true; j++) {
                count = sourceChannel.read(bbuff);
                if (count == -1) {
                    break;
                }
                for (int i = 0; i < BUFF_NO; i++) {
                    bbuff[i].flip();
                }
                if (BUFF_NO == 1) {
                    count = destinationChannel.write(bbuff[0]);
                } else {
                    count = destinationChannel.write(bbuff);
                }
                if (verbose) {
                    System.out.println("Current transfer count =  " + count + " Total: " + bytesNo.get());
                }
                if (count < 0) {
                    break;
                }
                bytesNo.addAndGet(count);
                for (int i = 0; i < BUFF_NO; i++) {
                    bbuff[i].clear();
                }
            }
        } catch (Throwable t) {
            System.err.println("Got exception: ");
            t.printStackTrace();
        } finally {
            System.out.flush();
            System.err.flush();
        }
    }
