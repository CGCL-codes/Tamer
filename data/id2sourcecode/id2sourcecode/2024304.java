    private void useAFileActionPerformed(java.awt.event.ActionEvent evt) {
        File[] files = ResourceEditorView.showOpenFileChooser("Border Image", ".png", ".jpg", ".gif", ".svg");
        if (files != null) {
            try {
                BufferedImage bi;
                if (files[0].getName().toLowerCase().endsWith(".svg")) {
                    try {
                        InputStream input = new FileInputStream(files[0]);
                        org.apache.batik.transcoder.image.PNGTranscoder t = new org.apache.batik.transcoder.image.PNGTranscoder();
                        org.apache.batik.transcoder.TranscoderInput i = new org.apache.batik.transcoder.TranscoderInput(input);
                        ByteArrayOutputStream bo = new ByteArrayOutputStream();
                        org.apache.batik.transcoder.TranscoderOutput o = new org.apache.batik.transcoder.TranscoderOutput(bo);
                        t.transcode(i, o);
                        bo.close();
                        input.close();
                        bi = ImageIO.read(new ByteArrayInputStream(bo.toByteArray()));
                    } catch (org.apache.batik.transcoder.TranscoderException ex) {
                        ex.printStackTrace();
                        throw new IOException(ex);
                    }
                } else {
                    bi = ImageIO.read(files[0]);
                }
                borderImage.setIcon(new ImageIcon(bi));
                borderImage.setBorder(null);
                borderImage.setPreferredSize(new Dimension(bi.getWidth(), bi.getHeight()));
                wiz.revalidate();
                borderImage.revalidate();
                wiz.repaint();
                borderImage.repaint();
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "IO Error: " + ex, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
