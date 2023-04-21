    private void checkView() {
        if (view == null || (System.currentTimeMillis() - lastFetched) > expiryTime) {
            view = bot.createScreenCapture(outOf);
            lastFetched = System.currentTimeMillis();
        }
    }
