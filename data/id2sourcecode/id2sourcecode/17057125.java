    public static void generate(int seed) throws SlickException {
        if (!initDone) throw new SlickException("Use init() first.");
        Random rnd = new Random(seed);
        int rocks = 11;
        for (int i = 0; i < rocks; i++) {
            int x = 46 + rnd.nextInt(W - 46);
            int y = 46 + rnd.nextInt(H - 46);
            Disc(x, y, 8, Properties.ids.get("stone"));
        }
        int trees = 10;
        int tree_height = 50;
        int tree_width = 50;
        int branches_height = 50;
        int branches_width = 50;
        for (int i = 0; i < trees; i++) {
            int y = tree_height + branches_height + rnd.nextInt(H - tree_height - branches_height);
            int x = tree_width + branches_width + rnd.nextInt(W - tree_width - branches_width);
            int tree_top = y - tree_height;
            for (int a = y - tree_height; a < y; a++) {
                map[x][a] = Properties.ids.get("wood");
                map[x - 1][a] = Properties.ids.get("wood");
                map[x + 1][a] = Properties.ids.get("wood");
            }
            Disc(x, y - (tree_height + 16), 32, Properties.ids.get("plant"));
        }
        int lakes = 1;
        for (int i = 0; i < lakes; i++) {
            int x = 46 + rnd.nextInt(W - 46);
            int y = 46 + rnd.nextInt(H - 46);
            Circle(x, y, 64, Properties.ids.get("stone"));
            Circle(x + 1, y, 64, Properties.ids.get("stone"));
            Disc(x, y, 60, Properties.ids.get("water"));
        }
        int house_width = 50;
        int house_height = 50;
        int x = 4 + rnd.nextInt(46);
        int y = 4 + rnd.nextInt(H - 4);
        for (int i = x; i < x + house_width; i++) {
            for (int a = y; a < y + house_height; a++) {
                map[i][a] = Properties.ids.get("house");
            }
        }
    }
