            public void run() {
                try {
                    serverNode.getChannelService().getChannel("foo");
                } catch (NameNotBoundException nnb) {
                    System.out.println("Got expected exception " + nnb);
                }
            }
