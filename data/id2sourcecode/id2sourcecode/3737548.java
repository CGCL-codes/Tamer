    static boolean canRewriteClass(String className, ClassLoader loader) {
        if (((loader == null) && !canRewriteBootstrap) || className.startsWith("java/lang/ThreadLocal")) {
            return false;
        }
        if (className.startsWith("ognl/")) {
            return false;
        }
        return true;
    }
