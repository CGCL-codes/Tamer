    public static String BaiKe(String unknown) {
        String encodeurl = "";
        long sTime = System.currentTimeMillis();
        long eTime;
        try {
            String regEx = "\\#(.+)\\#";
            String searchText = "";
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(unknown);
            if (m.find()) {
                searchText = m.group(1);
            }
            System.out.println("searchText :  " + searchText);
            encodeurl = URLEncoder.encode(searchText, "UTF-8");
            String url = "http://baike.baidu.com/view/4206.htm";
            System.out.println(url);
            HttpURLConnection conn = (HttpURLConnection) (new URL(url)).openConnection();
            conn.setConnectTimeout(10000);
            Parser parser = new Parser(conn);
            parser.setEncoding(parser.getEncoding());
            NodeFilter filtera = new TagNameFilter("DIV");
            NodeList nodes = parser.extractAllNodesThatMatch(filtera);
            String textInPage = "";
            if (nodes != null) {
                for (int i = 0; i < nodes.size(); i++) {
                    Node textnode = (Node) nodes.elementAt(i);
                    String temp = textnode.toPlainTextString();
                    textInPage += temp + "\n";
                    System.out.println(temp);
                }
            }
            String s = Replace(textInPage, searchText);
            eTime = System.currentTimeMillis();
            String time = "搜索用时:" + (eTime - sTime) / 1000.0 + "s";
            System.out.println(s);
            return time + "\r\n" + s;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
