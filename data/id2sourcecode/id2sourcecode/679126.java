    private void loadGamesList() {
        System.out.println("Trying to load games list");
        try {
            URL url = getResource("games.txt");
            LineNumberReader reader = new LineNumberReader(new InputStreamReader(url.openConnection().getInputStream()));
            String games;
            String disk;
            files = new Vector();
            while ((disk = reader.readLine()) != null) {
                disk = disk.trim();
                if (disk.toLowerCase().endsWith(".prg") || disk.toLowerCase().endsWith(".p00")) {
                    files.addElement(disk);
                    files.addElement(disk);
                } else {
                    games = reader.readLine();
                    if (games != null) {
                        games = games.trim();
                        StringTokenizer stok = new StringTokenizer(games, ",");
                        while (stok.hasMoreElements()) {
                            String game = stok.nextToken();
                            files.addElement(disk);
                            files.addElement(game);
                            System.out.println("Adding: " + game);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Can not load games..." + e);
            e.printStackTrace();
            System.out.println("No games to load...");
        }
    }
