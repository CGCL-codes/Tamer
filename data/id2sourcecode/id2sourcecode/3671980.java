    public void run() {
        dout.write("starting (thread,activeBidCount)=" + Thread.currentThread().getName() + "," + bidManager.getBidCount());
        Bidder successfulBidder = null;
        while ((bidManager.getActiveBidCount() > 0) && bidManager.running) {
            dout.write("activeBidCount = " + bidManager.getActiveBidCount());
            if ((successfulBidder = bidManager.bidForRole()) != null) {
                dout.write("starting (3)");
                interact(successfulBidder);
            }
        }
        dout.write("finished");
    }
