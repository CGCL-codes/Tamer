    public static void main(String[] args) {
        for (String arg : args) {
            File in = new File(arg);
            File out = new File(in.getParent(), in.getName() + ".norm.wtq.xml");
            new GameWriter().writeGames(out, new GameReader().readGames(in));
        }
    }
