    public void autoPlay(String url) {
        try {
            URL url2 = new URL(url);
            PartwiseParser parser = new PartwiseParser();
            ScoreRenderer renderer = new ScoreRenderer();
            parser.addPrintListener(renderer);
            parser.parse(url2.openStream());
            page = new BrowPage(renderer.getScore());
            setLayout(null);
            page.setBounds(0, 0, 800, 600);
            add(page);
            repaint();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "ERROR", "警告", JOptionPane.WARNING_MESSAGE);
            e.printStackTrace();
        }
    }
