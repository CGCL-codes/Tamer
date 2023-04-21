    public JScrollPane getJScrollPane() {
        if (jScrollPane == null) {
            ImageIcon icon = null;
            try {
                try {
                    URL url = new URL(addressURL);
                    URLConnection conn = null;
                    conn = url.openConnection();
                    InputStream in = conn.getInputStream();
                    in.close();
                    icon = new ImageIcon(url);
                    BufferedImage bufImage = ImageIO.read(url);
                    BufferedImage bufImage1 = ImagePanel.resizeAnImage(bufImage, zoom);
                    icon = new ImageIcon(bufImage1);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                JLabel jLabelImage = new JLabel(icon);
                if (icon != null) jLabelImage.setText(java.util.ResourceBundle.getBundle("com/jjcp/resources/Strings").getString("SELECTED_IMAGE.")); else jLabelImage.setText(java.util.ResourceBundle.getBundle("com/jjcp/resources/Strings").getString("SELECTED_IMAGE_CAN_NOT_BE_SHOWED."));
                jLabelImage.setForeground(Color.blue);
                jLabelImage.setHorizontalAlignment(SwingConstants.CENTER);
                jLabelImage.setHorizontalTextPosition(SwingConstants.CENTER);
                jLabelImage.setVerticalTextPosition(SwingConstants.BOTTOM);
                jLabelImage.setVerticalAlignment(SwingConstants.CENTER);
                jLabelImage.setFont(new Font(java.util.ResourceBundle.getBundle("com/jjcp/resources/Strings").getString("LUCIDA_GRANDE"), Font.PLAIN, 10));
                if (icon != null) {
                    jLabelImage.setPreferredSize(new Dimension(icon.getIconWidth() + 10, icon.getIconHeight() + 10));
                    jScrollPane = new JScrollPane(jLabelImage);
                    jScrollPane.setSize(new Dimension(icon.getIconWidth() + 20, icon.getIconHeight() + 20));
                    this.setSize(new Dimension(icon.getIconWidth() + 30, icon.getIconHeight() + 50));
                } else jScrollPane = new JScrollPane(jLabelImage);
            } catch (Exception e) {
                e.printStackTrace();
                JLabel jLabelImage = new JLabel(java.util.ResourceBundle.getBundle("com/jjcp/resources/Strings").getString("SELECTED_IMAGE_CAN_NOT_BE_SHOWED."));
                jLabelImage.setForeground(Color.blue);
                jLabelImage.setHorizontalAlignment(SwingConstants.CENTER);
                jLabelImage.setHorizontalTextPosition(SwingConstants.CENTER);
                jLabelImage.setVerticalAlignment(SwingConstants.CENTER);
                jLabelImage.setVerticalTextPosition(SwingConstants.BOTTOM);
                jLabelImage.setFont(new Font(java.util.ResourceBundle.getBundle("com/jjcp/resources/Strings").getString("LUCIDA_GRANDE"), Font.PLAIN, 10));
                jLabelImage.setPreferredSize(new Dimension(100, 20));
                jScrollPane = new JScrollPane(jLabelImage);
            }
        }
        return jScrollPane;
    }
