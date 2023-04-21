    private void createNote(long[][][] nons, double endTime, ShortMessage mess, Part[] p) {
        Phrase phr = new Phrase(nons[mess.getChannel()][mess.getData1()][1] * 1.0 / this.res * 1.0);
        Note n = new Note();
        n.setPitch(mess.getData1());
        n.setDuration(endTime - phr.getStartTime());
        n.setDynamic((int) nons[mess.getChannel()][mess.getData1()][0]);
        phr.add(n);
        p[mess.getChannel()].add(phr);
        nons[mess.getChannel()][mess.getData1()][0] = -1;
        nons[mess.getChannel()][mess.getData1()][1] = -1;
    }
