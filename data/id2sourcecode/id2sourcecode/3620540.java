    public Document getDocument(String url, MyHandler handler) {
        Document doc = null;
        docIsOk = false;
        HttpGet get = new HttpGet(url);
        Log.d(D, "��ʼ��ȡ��ҳ��" + url);
        InputStream in = null;
        ByteArrayOutputStream out = null;
        try {
            HttpResponse response = hc.execute(get);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                Log.d(D, "��ȡ��ҳ�ɹ�����ʼ������ҳ��" + url);
                if (handler != null) handler.updateProgressBar(20);
                HttpEntity en = response.getEntity();
                out = new ByteArrayOutputStream();
                in = en.getContent();
                long total = en.getContentLength();
                byte[] buffer = new byte[1024];
                int count = 0;
                int length = -1;
                while ((length = in.read(buffer)) != -1) {
                    out.write(buffer, 0, length);
                    count += length;
                    int complete = (int) (count / total) * 60;
                    if (handler != null) handler.updateProgressBar(complete + 20);
                }
                String str = new String(out.toByteArray(), "gb2312");
                doc = Jsoup.parse(str);
                docIsOk = true;
            }
        } catch (Exception e) {
            if (e instanceof org.apache.http.conn.ConnectTimeoutException) Log.d(D, "connect to " + url + " timed out.");
            if (e instanceof java.net.SocketTimeoutException) Log.d(D, "�ȴ�ͻ����ӳ�ʱ��");
            e.printStackTrace();
        }
        return doc;
    }
