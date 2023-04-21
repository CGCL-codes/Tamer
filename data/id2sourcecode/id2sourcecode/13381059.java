    public static void main(String[] args) throws Exception {
        if (args == null || args.length < 1) {
            System.err.println("Usage: java gnu.beanfactory.Startup [class|url] [args...]");
            System.err.println();
            return;
        }
        String target = args[0];
        String[] shifted = new String[args.length - 1];
        for (int i = 0; i < args.length - 1; i++) {
            shifted[i] = args[i + 1];
        }
        if (target.startsWith("bean:")) {
            execRunnableBean(target, shifted);
        } else {
            initializeAndInvokeMain(target, shifted);
        }
    }
