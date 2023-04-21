        public MyGamePanel() {
            font = new Font("lucida sans regular", Font.PLAIN, 16);
            Random rand = new Random();
            me = new Player();
            me.x = 10 + rand.nextInt(300 - 20);
            me.y = 10 + rand.nextInt(300 - 20);
            me.latestKey = (char) ('A' + rand.nextInt(26));
            players.put("me", me);
        }
