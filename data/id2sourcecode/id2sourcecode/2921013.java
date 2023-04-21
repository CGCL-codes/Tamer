    public Prediction predict(long userId, long itemId, boolean includePrediction) {
        long start = System.currentTimeMillis();
        Double user_mean = (this.userMean ? collaborativeService.getUserMeanRating(userId) : null);
        Double item_mean = (this.itemMean ? collaborativeService.getItemMeanRating(itemId) : null);
        Double val = null;
        if (userMean) {
            if (itemMean) {
                val = (user_mean + item_mean) / 2;
                log.debug("Mean user/item prediction " + val);
            } else {
                val = user_mean;
                log.debug("Mean user prediction " + val);
            }
        } else {
            if (itemMean) {
                val = item_mean;
                log.debug("Mean item prediction " + val);
            }
        }
        logPrediction(0, 0, 0, (System.currentTimeMillis() - start), "PPPP");
        return returnPrediction(userId, itemId, val);
    }
