    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == stats_button) {
            String item = (String) (cb.getSelectedItem());
            if (!item.startsWith("http://")) return;
            if (item.endsWith(".pls")) {
                item = fetch_pls(item);
                if (item == null) return;
            } else if (item.endsWith(".m3u")) {
                item = fetch_m3u(item);
                if (item == null) return;
            }
            byte[] foo = item.getBytes();
            for (int i = foo.length - 1; i >= 0; i--) {
                if (foo[i] == '/') {
                    item = item.substring(0, i + 1) + "stats.xml";
                    break;
                }
            }
            System.out.println(item);
            try {
                URL url = null;
                if (running_as_applet) url = new URL(getCodeBase(), item); else url = new URL(item);
                BufferedReader stats = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream()));
                while (true) {
                    String bar = stats.readLine();
                    if (bar == null) break;
                    System.out.println(bar);
                }
            } catch (Exception ee) {
            }
            return;
        }
        String command = ((JButton) (e.getSource())).getText();
        if (command.equals("start") && player == null) {
            play_sound();
        } else if (player != null) {
            stop_sound();
        }
    }
