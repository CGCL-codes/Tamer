    public void auto(String url) {
        if (player.getSingal()) player.stop();
        try {
            URL url2 = new URL(url);
            if (url.toLowerCase().endsWith(".mid") || url.toLowerCase().endsWith(".mid")) player.setSequence(MidiSystem.getSequence(url2.openStream())); else {
                PartwiseParser parser = new PartwiseParser();
                MidiRenderer renderer = new MidiRenderer();
                parser.addMusicParserListener(renderer);
                parser.parse(url2.openStream());
                player.setSequence(renderer.getSequence());
            }
            player.play();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "ERROR", "警告", JOptionPane.WARNING_MESSAGE);
        }
    }
