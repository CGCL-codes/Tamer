    @Override
    public Object compute() {
        Object[] args = new Object[fixed.length - 1];
        for (int i = 0; i < args.length; i++) args[i] = fixed[i + 1].computeIfExpression();
        return ((ProvaOperator) ((ProvaConstant) fixed[0]).getObject()).evaluate(args);
    }
