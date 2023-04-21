    @Override
    public void init(GeneratorContext context) {
        incrementGenerator = incrementDistribution.createNumberGenerator(Long.class, minIncrement, maxIncrement, granularity, false);
        if (minIncrement < 0 && maxIncrement <= 0) initial = max; else if (minIncrement >= 0 && maxIncrement > 0) initial = min; else initial = (min + max) / 2;
        next = initial;
        incrementGenerator.init(context);
        super.init(context);
    }
