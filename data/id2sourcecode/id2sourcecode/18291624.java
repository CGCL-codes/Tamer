    public Map load(String filePath) throws IOException {
        System.out.println(filePath);
        URL url = getClass().getResource("/map/" + filePath);
        System.out.println(url);
        if (url == null) {
            return null;
        }
        InputStream in = url.openStream();
        String text = NInputStream.getText(in);
        String[] lines = text.split("\n");
        String[] firstLineTokens = lines[0].split(",");
        int width = Integer.parseInt(firstLineTokens[0].trim());
        int height = Integer.parseInt(firstLineTokens[1].trim());
        Map map = new Map(width, height);
        for (int y = 0; y < height; y++) {
            char[] chs = lines[y + 1].toCharArray();
            for (int x = 0; x < width; x++) {
                char ch = chs[x];
                if (ch == '%') {
                    Box box = new Box(x, y);
                    map.addBox(box);
                    map.setPlatform(new Platform(x, y));
                } else if (ch == '*') {
                    map.setBoard(new Board(x, y));
                } else if (ch == '@') {
                    map.setPlatform(new Platform(x, y));
                } else if (ch == '#') {
                    map.setGoal(new Goal(x, y));
                } else if (ch == '|') {
                    Worker w = new Worker(x, y);
                    map.setWorker(w);
                }
            }
        }
        return map;
    }
