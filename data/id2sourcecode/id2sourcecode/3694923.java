    public void newGroups(final Date date) throws NntpNotConnectedException {
        if (usedConnections.size() == 0) {
            throw new NntpNotConnectedException();
        }
        Thread t = new Thread() {

            private NNTPConnection con = usedConnections.get(0);

            public void run() {
                LineIterator lit;
                try {
                    lit = con.newGroups(date, null);
                    System.out.println("NEW NEWS: ");
                    while (lit.hasNext()) {
                        System.out.println(lit.nextLine());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }
