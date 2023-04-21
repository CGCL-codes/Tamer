    private static boolean shouldCheckGui(IJavaRule rule) {
        for (Method check : rule.getClass().getMethods()) {
            String method = check.getName();
            if (method.equals("readProperties") || method.equals("writeProperties") || method.equals("createPreferencePanel")) {
                if (!check.getDeclaringClass().getSimpleName().equals("RuleBase")) {
                    return true;
                }
            }
        }
        return false;
    }
