    private Expression rewriteCondition(Expression expr, Expression message, boolean explicit) {
        if (expr instanceof MethodCallExpression && !((MethodCallExpression) expr).isSpreadSafe()) return rewriteMethodCondition((MethodCallExpression) expr, message, explicit);
        if (expr instanceof StaticMethodCallExpression) return rewriteStaticMethodCondition((StaticMethodCallExpression) expr, message, explicit);
        return rewriteOtherCondition(expr, message);
    }
