    public ArrayList<Review> getReviews() {
        long startTime = System.currentTimeMillis();
        ArrayList<Review> results = null;
        try {
            ReviewHandler handler = new ReviewHandler();
            URL url = new URL(this.query);
            Xml.parse(url.openStream(), Xml.Encoding.UTF_8, handler);
            results = handler.getReviews();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        long duration = System.currentTimeMillis() - startTime;
        Log.v(Constants.LOG_TAG, " " + ReviewFetcher.CLASSTAG + " call and parse duration - " + duration);
        return results;
    }
