    public static ArrayList<fThread> loadThreads(String boardUrl) {
        String data = "";
        try {
            URL url = new URL(boardUrl);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()), 1000000);
            char[] cbuf = new char[1000000];
            in.read(cbuf, 0, 1000000);
            data = new String(cbuf);
            in.close();
            in = null;
            cbuf = null;
        } catch (IOException e) {
        }
        data = StringEscapeUtils.unescapeHtml(data.substring(data.indexOf("<tr class=\"js-topic-row\" ") + 25, data.indexOf("</tbody>")));
        String[] datas = data.split("</tr>");
        datas[datas.length - 1] = null;
        ArrayList<fThread> threads = new ArrayList<fThread>();
        fThread t;
        for (String i : datas) {
            if (i != null) {
                t = new fThread();
                t.thumbUrl = i.substring(i.indexOf("<img src=\"") + 10, i.indexOf("\" alt=\"\" /></td>"));
                i = i.substring(i.indexOf("<td class=\"title\">") + 18);
                t.url = "http://www.giantbomb.com" + i.substring(i.indexOf("<a href=\"") + 9, i.indexOf("\">"));
                t.title = i.substring(i.indexOf("\">") + 2, i.indexOf("</a>"));
                i = i.substring(i.indexOf("class=\"board\">") + 14);
                t.section = i.substring(0, i.indexOf("</a>"));
                i = i.substring(i.indexOf("<td class=\"time\">") + 17);
                t.author = i.substring(i.indexOf("\">") + 2, i.indexOf("</a>"));
                t.authorDate = i.substring(i.indexOf("<div>") + 5, i.indexOf("</div>"));
                i = i.substring(i.indexOf("<td class=\"time\">") + 17);
                t.lastPoster = i.substring(i.indexOf("\">") + 2, i.indexOf("</a>"));
                t.lastPosterDate = i.substring(i.indexOf("<div>") + 5, i.indexOf("</div>"));
                t.views = i.substring(i.indexOf("<td class=\"count\"><div>") + 23, i.indexOf("</div></td>"));
                i = i.substring(i.indexOf("</div></td>") + 12);
                t.posts = i.substring(i.indexOf("<td class=\"count\"><div>") + 23, i.indexOf("</div></td>"));
                threads.add(t);
            }
        }
        return threads;
    }
