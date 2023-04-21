    private void timeout() {
        synchronized (chBuffer) {
            chBuffer.setLength(0);
            switch(state) {
                case 1:
                    lblChannel.setText(getChannelFormated());
                    lblChannel.setColor(Color.RED);
                    lblChannel.setVisible(true);
                    thread.countDown();
                    state = 3;
                    break;
                case 2:
                    lblChannel.setVisible(false);
                    state = 0;
                    break;
                case 3:
                    lblChannel.setVisible(false);
                    state = 0;
                    break;
            }
        }
    }
