    private boolean wasDateReached(Date to) {
        try {
            return to.before(this.dateFormatter.parse(this.dateFormatter.format(new Date())));
        } catch (ParseException pe) {
            throw new RuntimeException(pe);
        }
    }
