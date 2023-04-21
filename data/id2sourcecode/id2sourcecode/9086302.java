    public static void main(String[] args) {
        try {
            CommandExecutorHanlder handler = (CommandExecutorHanlder) Class.forName(args[0]).newInstance();
            String[] nargs = new String[args.length - 1];
            for (int i = 0; i < args.length - 1; i++) nargs[i] = args[i + 1];
            new Edits(handler);
            handle(nargs, handler);
        } catch (Exception e) {
            e.printStackTrace();
            Logger logger = Logger.getLogger("edits.main");
            logger.debug(e.getMessage(), e);
            System.out.println("Error: " + e.getMessage());
        }
    }
