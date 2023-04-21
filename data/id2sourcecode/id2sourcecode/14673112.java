    protected SComponent createExample() {
        SPanel panel = new SPanel(new SGridLayout(2, 1, 5, 5));
        SPanel inputPanel = new SPanel(new SGridLayout(1, 2, 5, 5));
        panel.add(inputPanel);
        final SPanel imagePanel = new SPanel(new SBorderLayout());
        panel.add(imagePanel);
        final STextField urlInput = new STextField("http://wingsframework.org/wingset/icons/wings-logo.png");
        SButton button = new SButton("Open");
        button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                BufferedImage image = null;
                try {
                    URL url = new URL(urlInput.getText());
                    InputStream in = url.openStream();
                    image = ImageIO.read(in);
                } catch (MalformedURLException ex) {
                    ex.printStackTrace();
                    SOptionPane.showMessageDialog(null, ex.getMessage());
                } catch (IOException ex) {
                    ex.printStackTrace();
                    SOptionPane.showMessageDialog(null, ex.getMessage());
                }
                if (image != null) {
                    XZoomableImage zoomableImage = new XZoomableImage(image);
                    zoomableImage.setPreviewImageMaxDimension(new SDimension(160, 120));
                    zoomableImage.setDetailImageMaxDimension(new SDimension(400, 300));
                    imagePanel.removeAll();
                    imagePanel.add(zoomableImage, BorderLayout.CENTER);
                }
            }
        });
        inputPanel.add(urlInput);
        inputPanel.add(button);
        return panel;
    }
