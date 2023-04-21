    private Object readRewriteRule(String name, Attributes attr, Constructor.RewriteRule rewriteRule) throws Exception {
        return this.startElementTerm(rewriteRule.getTerm(), name, attr);
    }
