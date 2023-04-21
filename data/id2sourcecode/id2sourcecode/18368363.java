    public static ArrayList<Post> loadPosts(String threadUrl) {
        System.out.println("url: " + threadUrl);
        data = "";
        try {
            URL url = new URL(threadUrl);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()), 1000000);
            char[] cbuf = new char[100000];
            in.read(cbuf, 0, 100000);
            data = new String(cbuf);
            in.close();
            in = null;
            cbuf = null;
        } catch (IOException e) {
            System.out.println("IOEXCEPTION");
        }
        data = data.substring(data.indexOf("class=\"post\">") + 13, data.indexOf("<div id=\"\" class=\"content-pod menu\">"));
        datas = data.split("class=\"post\">");
        posts = new ArrayList<Post>();
        for (String i : datas) {
            if (i != null) {
                Post p = new Post();
                i = i.substring(i.indexOf("Post by <a href=\"/profile/"));
                p.author = i.substring(i.indexOf("\">") + 2, i.indexOf("</a>"));
                p.thumbUrl = i.substring(i.indexOf("style=\"background-image: url(") + 29, i.indexOf(");\"></a>"));
                i = i.substring(i.indexOf("class=\"wiki-content\">"));
                p.post = "";
                p.post = i.substring(21, i.indexOf("</div>")).trim();
                System.out.println(i.indexOf("</span>"));
                i = i.substring(i.indexOf("<span class=\"post-date\">"));
                p.authorDate = i.substring(24, i.indexOf("</span>"));
                posts.add(p);
            }
        }
        return posts;
    }
