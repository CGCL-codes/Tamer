    public Block<?> getTestBlockForCon(Constructor<T> pCon, Expression<?>[] curPlans) {
        notNull(pCon);
        notNull(curPlans);
        final Class<T> testeeType = pCon.getDeclaringClass();
        final Block<?> b = new BlockImpl(testeeType, pCon, testBlockSpaces);
        final BlockStatement<?>[] bs = new BlockStatement[curPlans.length + 1];
        final Variable<?>[] ids = new Variable[curPlans.length];
        Class<?>[] paramsTypes = pCon.getParameterTypes();
        for (int i = 0; i < curPlans.length; i++) {
            ids[i] = b.getNextID(paramsTypes[i]);
            bs[i] = new LocalVariableDeclarationStatement(ids[i], curPlans[i]);
        }
        ConstructorCall<T> conPlan = null;
        if (typeGraph.getWrapper(pCon.getDeclaringClass()).isInnerClass()) {
            Expression[] paramPlans = new Expression[curPlans.length - 1];
            for (int j = 0; j < paramPlans.length; j++) {
                paramPlans[j] = ids[j + 1];
            }
            conPlan = new ConstructorCall<T>(testeeType, pCon, paramPlans, ids[0]);
        } else {
            conPlan = new ConstructorCall<T>(testeeType, pCon, ids);
        }
        bs[curPlans.length] = new ExpressionStatement<T>(conPlan);
        List<BlockStatement> blockStatements = new LinkedList<BlockStatement>();
        for (BlockStatement blockStatement : bs) blockStatements.add(blockStatement);
        b.setBlockStmts(blockStatements);
        return b;
    }
