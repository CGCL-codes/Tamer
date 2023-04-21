        public void actionPerformed(ActionEvent e) {
            String channelName = new String();
            channelName = getChannelTitle(tf_url.getText());
            if (channelName != null) {
                tf_name.setText(channelName);
            } else {
                tf_url.selectAll();
            }
        }
