    private void usage() {
        debug(0, "usage: DiskTester [-qvicVrw] [-n count] id\n" + "  -q   less verbose debugging\n" + "  -v   more verbose debugging\n" + "  -c   random reads/writes are clustered (default is uniform)\n" + "  -i   start by initializing all blocks with a known value\n" + "  -V   verify all reads to ensure they return the right value\n" + "  -r   only read\n" + "  -w   only write\n" + "           default is read or write with equal probability\n" + "  -n N number of random reads/writes (defautl is zero)\n" + "  id   an id to identify this DiskTester instance\n");
        System.exit(1);
    }
