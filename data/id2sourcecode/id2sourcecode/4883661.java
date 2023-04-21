    private void txtPostActionPerformed(java.awt.event.ActionEvent evt) {
        ChannelBuilder builder = new ChannelBuilder();
        ChannelIF channelIF = this.channelSubscription.getChannel();
        ItemIF itemIF = builder.makeItem(this.txtTitle.getText(), this.txtData.getText(), channelIF.getLocation());
        channelIF.addItem(itemIF);
        if (this.channelSubscription instanceof ChannelSubscription) {
            ChannelSubscription cs = (ChannelSubscription) this.channelSubscription;
            try {
                cs.store();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error storing channel: " + e.getMessage(), "Channel Update Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        this.txtData.setText("");
        this.txtTitle.setText("");
    }
