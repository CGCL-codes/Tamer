    public Operator addRandomValidOperatorTo(OperatorChain chain, RandomGenerator random) throws UndefinedParameterError, NoValidOperatorException {
        int which = random.nextInt(MAX_VALID_TYPE + 1);
        Operator vsOp = createValidOperator(which, random);
        addOperator(chain, vsOp, random);
        return vsOp;
    }
