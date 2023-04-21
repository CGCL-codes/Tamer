    public RewriteBuilder<SourceElement> withStatement(StatementBuilder<SourceElement> statement) {
        if (rewrite.getStatement() != null) {
            throw new IllegalStateException("The rewrite statement was already set.");
        }
        rewrite.setStatement(statement.build());
        return this;
    }
