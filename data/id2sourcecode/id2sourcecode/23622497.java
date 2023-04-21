    public static void main(String argv[]) throws IOException {
        int i = 0;
        while (i < argv.length) {
            if (argv[i].equals("-dbg")) {
                i++;
                debugger.debugClass(argv[i], true);
            } else break;
            i++;
        }
        Mediaserver[] servs = Synch.getMediaservers(argv, i);
        if (servs.length < 1) {
            pa("Please supply a Mediaserver data pool directory containing" + "client space.");
            System.exit(1);
        } else if (servs.length == 1) {
            ms = servs[0];
        } else {
            Mediaserver first = servs[0];
            Mediaserver[] other = new Mediaserver[servs.length - 1];
            for (int j = 0; j < other.length; j++) other[j] = servs[j + 1];
            ms = new MultiplexingMediaserver(first, null, other);
        }
        Mediaserver.Id spaceId = getPointer();
        pa("Starting with space\n" + spaceId.getString());
        space = new PermanentSpace(ms, spaceId);
        clientCell = space.getHomeCell().s(d1);
        if (clientCell == null) {
            pa("No client cell in space");
            System.exit(1);
        }
        client.start();
    }
