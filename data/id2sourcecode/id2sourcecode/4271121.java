    public static void installRewrite(ClassTraversal.Visitor rewrite) {
        Rewriter r = (Rewriter) Thread.currentThread().getContextClassLoader();
        r.installPreRewriteTraversal(rewrite);
    }
