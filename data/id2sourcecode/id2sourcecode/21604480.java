    public void print() {
        System.out.println("Ticks: total " + totalTicks + ", kernel " + kernelTicks + ", user " + userTicks);
        System.out.println("Disk I/O: reads " + numDiskReads + ", writes " + numDiskWrites);
        System.out.println("Console I/O: reads " + numConsoleReads + ", writes " + numConsoleWrites);
        System.out.println("Paging: page faults " + numPageFaults + ", TLB misses " + numTLBMisses);
        System.out.println("Network I/O: received " + numPacketsReceived + ", sent " + numPacketsSent);
    }
