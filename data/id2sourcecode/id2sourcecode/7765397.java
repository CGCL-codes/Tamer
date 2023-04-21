    private void testConnectionButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if (!txtURL.getInputVerifier().verify(txtURL)) {
            return;
        }
        SyndFeedInput input = new SyndFeedInput();
        try {
            URL url = new URL(txtURL.getText() + "/rssAll");
            URLConnection urlConnection = url.openConnection();
            urlConnection.setConnectTimeout(5000);
            input.build(new XmlReader(urlConnection));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, Messages.getString("ht.config.testUrl.failed") + txtURL.getText() + "/rssAll" + "'\n" + e.getLocalizedMessage(), Messages.getString("ht.config.testUrl.failed.title"), JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }
        JOptionPane.showMessageDialog(this, Messages.getString("ht.config.testUrl.success") + txtURL.getText() + "/rssAll", Messages.getString("ht.config.testUrl.success.title"), JOptionPane.INFORMATION_MESSAGE);
    }
