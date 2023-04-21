    @Override
    public Object run(Object parameters, HashMap<String, Object> shared) {
        URLExploreJobInput input = (URLExploreJobInput) parameters;
        URL exploreurl = input.exploreurl;
        System.out.println("Story@" + (new Date(input.time)) + ":" + exploreurl);
        BufferedReader reutersreader = null;
        try {
            reutersreader = new BufferedReader(new InputStreamReader(exploreurl.openStream()));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        Scanner s = new Scanner(reutersreader);
        Boolean done = false;
        Boolean started = false;
        String contents = null;
        s.useDelimiter("[\n\r]+");
        while (s.hasNext() && !done) {
            String next = s.next();
            if (next.contains("quoteProfile.css") && started) {
                done = true;
            } else if (started) {
                contents = contents.concat(next);
            } else if (next.contains("<span id=\"midArticle_start\">")) {
                contents = new String(s.next());
                started = true;
            }
        }
        if (s != null) {
            s.close();
        }
        if (reutersreader != null) {
            try {
                reutersreader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        URLExploreJobOutput output = new URLExploreJobOutput();
        output.content = contents;
        output.time = input.time;
        output.title = input.title;
        return output;
    }
