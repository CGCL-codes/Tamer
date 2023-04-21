    public static void main(String[] args) {
        String cmd, usage = "Usage:\n\t(i)nsert OFFSET TEXT\n\t" + "(p)rint aLL ISegments | vISIBLE ISegments | mSET INodes\n\t" + "(q)uit";
        MSET<StringSequence> mset = new MSET<StringSequence>(1, new StringSequence(""));
        Scanner in = new Scanner(System.in);
        System.out.println(usage);
        while (in.hasNext()) {
            cmd = in.next();
            if (cmd.equalsIgnoreCase("q") || cmd.equalsIgnoreCase("QUIT")) return; else if (cmd.equalsIgnoreCase("i") || cmd.equalsIgnoreCase("INSERT")) System.out.println(mset.insertByLocal(in.nextInt(), new StringSequence(in.nextLine().substring(1)))); else if (cmd.equalsIgnoreCase("p") || cmd.equalsIgnoreCase("PRINT")) {
                cmd = in.next();
                if (cmd.equalsIgnoreCase("visible") || cmd.equalsIgnoreCase("v")) System.out.println(mset.globalSTree.toString(true)); else if (cmd.equalsIgnoreCase("all") || cmd.equalsIgnoreCase("a")) System.out.println(mset.globalSTree.toString()); else System.out.println(mset);
            } else System.out.println(usage);
        }
    }
