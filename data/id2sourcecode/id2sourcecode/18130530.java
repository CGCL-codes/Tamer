    public static void main(String[] args) {
        URL url;
        try {
            url = new URL("http://www.onjava.com/pub/a/onjava/2003/03/05/lucene.html?page=2");
            URLConnection urlConnection = url.openConnection();
            BufferedReader htmlPage = new BufferedReader(new InputStreamReader(url.openStream()));
            HTMLParser htmlparser = new HTMLParser(htmlPage);
            LineNumberReader reader = new LineNumberReader(htmlparser.getReader());
            for (String l = reader.readLine(); l != null; l = reader.readLine()) {
                System.out.println(l);
            }
            htmlPage.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
