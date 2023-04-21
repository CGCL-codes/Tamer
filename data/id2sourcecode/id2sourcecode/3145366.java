    public synchronized void process() {
        int mid = 1 + (XE - 1) / 2;
        for (int i = rand(8); i > 0; i--) {
            int c = rand(XE - 1 - 15 - 15) + 15;
            for (int j = 0; j < 10; j++) off[BOTTOM + c + j] = colorit(180 + rand(76));
        }
        if (burn > 0) {
            splash();
            burn--;
        }
        splashRandom();
        smoothBottom();
        for (int i = 1; i < mid; i++) processline(i);
        for (int i = XE; i >= mid; i--) processline(i);
    }
