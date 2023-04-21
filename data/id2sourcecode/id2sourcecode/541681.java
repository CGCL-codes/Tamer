    public static void main(String args[]) {
        if (args.length < 1) {
            System.out.println("USAGE: shatest <key> [prefix]");
            return;
        }
        try {
            helper = new SHA1Helper();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        if (args.length == 1) System.out.println(helper.digest("", args[0])); else System.out.println(helper.digest(args[1], args[0]));
    }
