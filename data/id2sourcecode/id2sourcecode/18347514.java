    private String borderImages(String content, final String nodeBase) {
        ArrayList<String> imgTags = new ArrayList<String>();
        for (int i = 0; i < content.length() - 4; i++) {
            if (content.substring(i, i + 4).equals("<img")) {
                for (int j = i; j < content.length(); j++) if (content.charAt(j) == '>') {
                    imgTags.add(content.substring(i, j));
                    break;
                }
            }
        }
        final ArrayList<String> imgUrls = new ArrayList<String>();
        for (String imgtag : imgTags) {
            int start = imgtag.indexOf("src=\"") + 5;
            int end = imgtag.indexOf("\"", start);
            imgUrls.add(imgtag.substring(start, end));
        }
        ArrayList<Thread> imgPreloaders = new ArrayList<Thread>();
        final ArrayList<String> imgNotFound = new ArrayList<String>();
        for (final String url : imgUrls) {
            Runnable preloadImage = new Runnable() {

                public void run() {
                    try {
                        URLConnection connection = new URL(nodeBase + url).openConnection();
                        connection.setConnectTimeout(100);
                        connection.getContent();
                        imgUrls.remove(url);
                    } catch (Exception e) {
                    }
                }
            };
            imgPreloaders.add(new Thread(preloadImage));
        }
        for (Thread t : imgPreloaders) t.start();
        for (Thread t : imgPreloaders) try {
            t.join(500);
        } catch (Exception e) {
            System.out.println("Error in Browser: borderImages: " + e);
        }
        for (String imgUrl : imgUrls) content = content.replaceAll(imgUrl + "\"", imgUrl + "\" border=3 ");
        return content;
    }
