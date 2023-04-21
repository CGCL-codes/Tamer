    private void callNotification(final int userID) {
        if (notificationTargets != null) {
            new Thread(new Runnable() {

                public void run() {
                    for (int i = 0, n = notificationTargets.length; i < n; i++) {
                        try {
                            URL url = new URL(notificationTargets[i], "?id=" + userID);
                            URLConnection conn = url.openConnection();
                            int length = conn.getContentLength();
                            conn.getInputStream().close();
                        } catch (Exception e) {
                            log.log(Level.WARNING, "could not notify " + notificationTargets[i], e);
                        }
                    }
                }
            }).start();
        }
    }
