    public void update(final Group group) throws NntpNotConnectedException {
        if (usedConnections.size() == 0) {
            throw new NntpNotConnectedException();
        }
        Thread t = new Thread() {

            private NNTPConnection con = usedConnections.get(0);

            public void run() {
                LineIterator ait;
                try {
                    ait = con.newNews(group.getName(), group.getLastUpdate(), null);
                    System.out.println("NEW NEWS: ");
                    while (ait.hasNext()) {
                        System.out.println(ait.nextLine());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();
        group.setLastUpdate(new Date());
    }
