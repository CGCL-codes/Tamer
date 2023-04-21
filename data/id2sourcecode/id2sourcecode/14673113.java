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
