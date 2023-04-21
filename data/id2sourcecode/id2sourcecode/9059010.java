    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.out.println("\nBoot (1999-11-27) David Wallace Croft (croft@orbs.com)");
            System.out.println("Updates available from \"http://www.orbs.com/\".\n");
            System.out.println("Bootstraps the main(args) method of a class available " + "from a web site.");
            System.out.println("Arguments:  URL [other...]");
            System.out.println("Example:  java -jar boot.jar " + "http://www.orbs.com/lib/Main.class username password");
            return;
        }
        byte[] data = downloadBytes(new URL(args[0]));
        if (data == null) {
            System.out.println("Unable to download \"" + args[0] + "\".");
            return;
        }
        String[] shiftedArgs = new String[args.length - 1];
        for (int i = 0; i < shiftedArgs.length; i++) {
            shiftedArgs[i] = args[i + 1];
        }
        new Boot().bootstrap(data, shiftedArgs);
    }
