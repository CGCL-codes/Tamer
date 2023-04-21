    public List<String> getImgsFromUrl(String url) {
        List<String> list = new ArrayList<String>();
        HttpGet get = new HttpGet(url);
        Log.d(D, "��ʼ������ҳ��" + url);
        try {
            HttpResponse response = hc.execute(get);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String str = EntityUtils.toString(response.getEntity());
                Document doc = Jsoup.parse(str);
                Elements elements = doc.getElementsByClass("txt_img");
                for (Element e : elements) {
                    Elements imgs = e.getElementsByTag("img");
                    for (Element img : imgs) {
                        Log.d(D, img.attr("src"));
                        list.add(img.attr("src"));
                    }
                }
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
