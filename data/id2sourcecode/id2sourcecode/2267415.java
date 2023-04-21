    public static void main(String[] args) {
        WordPath instance = new WordPath();
        int count;
        int nbRows = Integer.valueOf(args[0]).intValue();
        String[] grid = new String[nbRows];
        for (int i = 0; i < nbRows; i++) grid[i] = args[i + 1];
        String find = args[nbRows + 1];
        count = instance.countPaths(grid, find);
        System.out.println("I found " + count + " " + (count > 1 ? "paths" : "path") + " for this string:)\n");
    }
