    public void setTextureNode(TextureGraphNode n) {
        if (n == node) return;
        if (node != null) node.getChannel().removeChannelChangeListener(this);
        scrollPaneContent.removeAll();
        node = n;
        if (n != null) {
            node.getChannel().addChannelChangeListener(this);
            Channel c = n.getChannel();
            Vector<AbstractParam> params = c.getParameters();
            int x = 8;
            int y = 8;
            JLabel title = new JLabel(" Type: " + c.getClass().getSimpleName());
            title.setBorder(BorderFactory.createEtchedBorder());
            title.setBounds(x, y, 240, 24);
            y += 30;
            scrollPaneContent.add(title);
            Component editor;
            for (AbstractParam param : params) {
                editor = getEditorForParam(param);
                if (editor == null) {
                    editor = new JLabel(param.getName());
                    editor.setBounds(x, y, 128, 24);
                }
                editor.setLocation(x, y);
                y += editor.getHeight() + 4;
                scrollPaneContent.add(editor);
            }
            y += 8;
            scrollPaneContent.setPreferredSize(new Dimension(previewImageSize, y));
            scrollPaneContent.setSize(scrollPaneContent.getPreferredSize());
            previewPanel.setVisible(true);
        } else {
            scrollPaneContent.setPreferredSize(new Dimension(previewImageSize, 0));
            scrollPaneContent.setSize(scrollPaneContent.getPreferredSize());
            benchmarkLabel.setText("");
            previewPanel.setVisible(false);
        }
        channelChanged(null);
    }
