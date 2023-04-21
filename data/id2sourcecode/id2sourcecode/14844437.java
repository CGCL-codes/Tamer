    protected void prePersist(EntityManager em) throws Exception {
        ActivityBuilder builder = ActivityBuilder.instance();
        builder.type("type");
        builder.entityHandle("entityHandle");
        builder.user("user");
        builder.timestamp(new Date());
        builder.title("title");
        builder.digest("digest");
        builder.action(null);
        builder.url("url");
        builder.partition("partition");
        builder.dimension1("dimension1");
        builder.dimension2("dimension2");
        entity = builder.build();
    }
