    public static int read() throws IOException {
        int amt = 0;
        for (int i = 0; i < threadCount; i++) {
            if (incounts[i] < messageCount) {
                int avail = ins[i].available();
                while (avail >= writeMessageBytes.length) {
                    ins[i].read(dest, 0, writeMessageBytes.length);
                    if (dolog) System.out.println("client read " + incounts[i] + " on " + i);
                    incounts[i]++;
                    totalIn++;
                    avail = ins[i].available();
                }
                if (incounts[i] >= messageCount) {
                    ins[i].close();
                    outs[i].close();
                }
            }
            amt += incounts[i];
        }
        return amt;
    }
