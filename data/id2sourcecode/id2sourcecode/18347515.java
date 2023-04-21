                public void run() {
                    try {
                        URLConnection connection = new URL(nodeBase + url).openConnection();
                        connection.setConnectTimeout(100);
                        connection.getContent();
                        imgUrls.remove(url);
                    } catch (Exception e) {
                    }
                }
