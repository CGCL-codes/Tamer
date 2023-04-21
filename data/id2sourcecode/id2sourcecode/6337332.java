    private void loadExternalPage(String location) {
        try {
            if (location.startsWith("udp")) {
                VLCLauncher.launchVLC(location.toString());
            } else if (location.startsWith("http://")) {
                boolean isAudio = false;
                try {
                    URL url = new URL(location);
                    URLConnection connection = url.openConnection();
                    String contentType = connection.getHeaderField("Content-Type");
                    if (contentType != null && contentType.compareToIgnoreCase("audio/mpeg") == 0) {
                        isAudio = true;
                    } else {
                        byte[] buffer = new byte[3];
                        connection.getInputStream().read(buffer);
                        connection.getInputStream().close();
                        String protocol = new String(buffer);
                        if (protocol.compareToIgnoreCase("ICY") == 0) {
                            isAudio = true;
                        }
                    }
                } catch (Exception e) {
                }
                if (isAudio) {
                    AudioPlayer.getInstance().setURL(new URL(location));
                } else {
                    DataViewer.browse(new URL(location));
                }
            } else {
                DataViewer.browse(new URL(location));
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Unable to open the URL in an external program.", "Web Data Panel Error", JOptionPane.ERROR_MESSAGE);
        }
    }
