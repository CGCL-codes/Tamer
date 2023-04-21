    @Override
    public void setDataValue(final String name, final Object newValue) {
        Thread thread = new Thread() {

            @Override
            public void run() {
                if (serverIPAddress != null && !serverIPAddress.equals("")) {
                    try {
                        String urlStr = "http://" + serverIPAddress + ":8080" + "/settag?Tag=" + name + "&Value=" + newValue;
                        URL url = new URL(urlStr);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("GET");
                        if (handler.isDebugMode()) {
                            System.out.println("sending tag=" + name + " " + newValue);
                        }
                        connection.connect();
                        connection.getContent();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        };
        thread.start();
    }
