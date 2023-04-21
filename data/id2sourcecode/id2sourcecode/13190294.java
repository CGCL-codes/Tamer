    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == statsButton) {
            String item = (String) (cb.getSelectedItem());
            if (!item.startsWith("http://")) {
                return;
            }
            if (item.endsWith(".pls")) {
                item = fetchPls(item);
                if (item == null) {
                    return;
                }
            } else if (item.endsWith(".m3u")) {
                item = fetchM3u(item);
                if (item == null) {
                    return;
                }
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
                if (runningAsApplet) {
                    url = new URL(getCodeBase(), item);
                } else {
                    url = new URL(item);
                }
                BufferedReader stats = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream()));
                try {
                    while (true) {
                        String bar = stats.readLine();
                        if (bar == null) {
                            break;
                        }
                        System.out.println(bar);
                    }
                } finally {
                    stats.close();
                }
            } catch (IOException ee) {
            }
            return;
        }
        String command = ((JButton) (e.getSource())).getText();
        if (command.equals("start") && player == null) {
            playSound();
        } else if (player != null) {
            stopSound();
        }
    }
