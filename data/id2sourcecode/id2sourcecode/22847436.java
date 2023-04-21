    protected void runQuery() {
        this.response = actingContext.getChannel().onQuery(actingContext, query);
    }
