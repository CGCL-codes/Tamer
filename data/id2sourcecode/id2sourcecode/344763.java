    public static void main(String... args) throws Exception {
        String g, d, m, f;
        if (args.length > 3) {
            g = args[0];
            d = args[1];
            m = args[2];
            f = args[3];
        } else {
            g = readValue("G:");
            d = readValue("D:");
            m = readValue("M:");
            f = readValue("F:");
        }
        System.out.print("Wait please...");
        StringBuffer contStr = new StringBuffer();
        contStr.append("xajax=getPS&xajaxr=").append(new Date().getTime()).append("&xajaxargs[]=").append(g).append("&xajaxargs[]=").append(d).append("&xajaxargs[]=").append(m).append("&xajaxargs[]=").append(f);
        HttpURLConnection conn = connect("http://pbliga.com/mng_scout_ajax.php", "POST", "application/x-www-form-urlencoded; charset=UTF-8", contStr.toString(), 5000);
        InputStream is = conn.getInputStream();
        XPath xpath = XPathFactory.newInstance().newXPath();
        String value = xpath.evaluate("//xjx/cmd/text()", new InputSource(is));
        is.close();
        File htmlResult = new File(HTML_FILE_NAME);
        if (htmlResult.exists()) htmlResult.delete();
        htmlResult.createNewFile();
        FileWriter fw = new FileWriter(htmlResult);
        fw.write("<font color=\"red\" size=\"+3\">" + g + "-" + d + "-" + m + "-" + f + "</font><br/>");
        fw.write(value);
        fw.flush();
        fw.close();
        Runtime.getRuntime().exec(new String[] { "cmd", "/c", HTML_FILE_NAME });
    }
