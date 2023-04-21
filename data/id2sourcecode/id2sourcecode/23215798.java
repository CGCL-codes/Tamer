    public static void main(String[] _args) throws Exception {
        int pool = POOLSIZE;
        int buffer = BUFFERSIZE;
        int page = PAGESIZE;
        int min = MINSIZE;
        int max = MAXSIZE;
        String host = "localhost";
        int port = 3333;
        try {
            for (int i = 0; i < _args.length; i++) {
                if (_args[i].startsWith("-help")) {
                    help();
                } else if (_args[i].startsWith("-pool")) {
                    pool = Integer.parseInt(_args[i].substring(5));
                } else if (_args[i].startsWith("-buffer")) {
                    buffer = Integer.parseInt(_args[i].substring(7));
                } else if (_args[i].startsWith("-page")) {
                    page = Integer.parseInt(_args[i].substring(5));
                } else if (_args[i].startsWith("-min")) {
                    min = Integer.parseInt(_args[i].substring(4));
                } else if (_args[i].startsWith("-max")) {
                    max = Integer.parseInt(_args[i].substring(4));
                } else if (_args[i].startsWith("-host")) {
                    host = _args[i].substring(5);
                } else {
                    if (_args[i].startsWith("-port")) {
                        port = Integer.parseInt(_args[i].substring(5));
                    }
                }
            }
        } catch (Exception e) {
            help();
        }
        RemoteDatabase db = new RemoteDatabase();
        try {
            db.open(host, port);
        } catch (Exception e) {
            System.out.print("No db found...");
            System.exit(0);
        }
        db.reloadClasses();
        System.out.println("connected...");
        BLOBTest blob = new BLOBTest(db, pool, buffer, page * 1024, min, max);
        System.out.println("----------------------------------------------------------------");
        System.out.println("  B L O B Test  starting the testthread. Press ENTER to abort.\n");
        System.out.println("environment:");
        System.out.println("connected to db at " + host + ":" + port);
        System.out.println("a pool of " + pool + " Blobs will be managed. ");
        System.out.println("Streambuffersize = " + buffer + " byte");
        System.out.println("BlobPagesize = " + page + " kB");
        System.out.println("Blobs will be created in the range from " + min + " kB to " + max + " kB");
        System.out.println("----------------------------------------------------------------");
        blob.start();
        long start = System.currentTimeMillis();
        System.in.read();
        blob.halted = true;
        System.out.println("\nTest halted. Waiting for last databaseoperation to quit...");
        blob.join();
        long stop = System.currentTimeMillis();
        db.close();
        System.out.println("disconnected.");
        System.out.println("----------------------------------------------------------------");
        System.out.println("statistics:");
        stop = (stop - start) / 1000;
        start = stop / 60;
        stop = (stop - start * 60);
        System.out.println("test runs " + start + " min and " + stop + " seconds, ca." + blob.movement / 1024 / 1024 + " MB of data were moved (read + write)");
        System.out.println("average writespeed: " + blob.avWritespeed / (blob.writecount == 0 ? 1 : blob.writecount) + " Bytes/s");
        System.out.println("average readspeed:  " + blob.avReadspeed / (blob.readcount == 0 ? 1 : blob.readcount) + " Bytes/s");
    }
