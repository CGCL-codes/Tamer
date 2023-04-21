    public void run() {
        logger.info("Fetcher starting");
        try {
            URLConnection con = baseURL.openConnection();
            if (con instanceof HttpURLConnection) {
                con.setRequestProperty("Referer", baseURL.toString());
                con.setRequestProperty("User-Agent", "ComicViewer/1");
                System.out.println(con.getRequestProperties());
                con.connect();
                Object content = con.getContent();
                if (content instanceof InputStream) {
                    InputStream is = (InputStream) content;
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    StringBuffer page = new StringBuffer();
                    String line;
                    line = br.readLine();
                    while (line != null) {
                        page.append(line);
                        line = br.readLine();
                    }
                    logger.info("Trying to match with pattern: \"" + pattern.pattern());
                    Matcher m = pattern.matcher(page);
                    if (m.matches()) {
                        String imgloc = m.group(1);
                        String imgurl = imageBaseURL + imgloc;
                        logger.info("Image at: " + imgurl);
                        URLConnection imgcon = new URL(imgurl).openConnection();
                        imgcon.setRequestProperty("Referer", baseURL.toString());
                        imgcon.setRequestProperty("User-Agent", "ComicViewer/1");
                        imgcon.connect();
                        Toolkit tool = Toolkit.getDefaultToolkit();
                        Image img = tool.createImage((ImageProducer) imgcon.getContent());
                        logger.info(img.getWidth(null) + ", " + img.getHeight(null));
                        image = new ImageIcon(img);
                        isDone = true;
                    } else {
                        logger.warning("No match, pattern \"" + pattern.pattern() + "\"");
                    }
                } else {
                    logger.warning("Wrong IS type");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("Fetcher done");
    }
