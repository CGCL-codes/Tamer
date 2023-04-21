    private void loadPicture() throws IOException {
        String base_url = "http://antwrp.gsfc.nasa.gov/apod/";
        URL url = new URL(base_url);
        u.p("loading the page: " + url);
        Reader input = new InputStreamReader(url.openStream());
        char buf[] = new char[1024];
        StringBuffer page_buffer = new StringBuffer();
        while (true) {
            int n = input.read(buf);
            if (n < 0) {
                break;
            }
            page_buffer.append(buf, 0, n);
        }
        Pattern pattern = Pattern.compile("<a href=\"(.*)\">\\s*<IMG SRC=\".*\"");
        Matcher matcher = pattern.matcher(page_buffer);
        matcher.find();
        String img_url = base_url + matcher.group(1);
        u.p("loading the image: " + img_url);
        background = ImageIO.read(new URL(img_url));
        u.p("image loaded: " + background);
        getDesktopPane().repaint();
    }
