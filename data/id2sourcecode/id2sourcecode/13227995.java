    public Stats lookup(String username) {
        if (username != null && !username.isEmpty()) {
            try {
                URL url = new URL(Hiscores.HOST + Hiscores.GET + username);
                BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
                String[] html;
                int[] exps = new int[26];
                int[] lvls = new int[26];
                int[] ranks = new int[26];
                for (int i = 0; i < 26; i++) {
                    html = br.readLine().split(",");
                    exps[i] = Integer.parseInt(html[2]);
                    lvls[i] = Integer.parseInt(html[1]);
                    ranks[i] = Integer.parseInt(html[0]);
                }
                br.close();
                return new Stats(username, exps, lvls, ranks);
            } catch (IOException ignored) {
            }
        }
        return null;
    }
