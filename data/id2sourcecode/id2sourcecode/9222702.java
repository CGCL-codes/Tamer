    public static void main(String[] args) throws Exception {
        System.out.println("开始运行");
        File f = new File("stock4.properties");
        FileReader fr = new FileReader(f);
        BufferedReader br = new BufferedReader(fr);
        ArrayList<String> al = new ArrayList<String>();
        String s;
        while ((s = br.readLine()) != null) {
            String[] s1 = s.split(",");
            al.add("http://finance.sina.com.cn/realstock/company/" + s1[1] + s1[0] + "/ggtj.shtml");
        }
        br.close();
        fr.close();
        final ArrayList<StockA> stocksResult = new ArrayList<StockA>();
        HttpAsyncClient httpclient = new DefaultHttpAsyncClient();
        httpclient.start();
        try {
            ArrayList<HttpGet> httpgets = new ArrayList<HttpGet>();
            Iterator<String> i = al.iterator();
            while (i.hasNext()) {
                httpgets.add(new HttpGet(i.next()));
            }
            long t1 = System.currentTimeMillis();
            Time t = new Time(t1);
            System.out.println("Start time " + t.toString());
            final CountDownLatch latch = new CountDownLatch(httpgets.size());
            for (final HttpGet request : httpgets) {
                httpclient.execute(request, new FutureCallback<HttpResponse>() {

                    public void completed(final HttpResponse response) {
                        System.out.println(count + "completed" + request.getRequestLine() + "->" + response.getStatusLine());
                        count++;
                        HttpEntity he = response.getEntity();
                        try {
                            String resp = EntityUtils.toString(he, "gb2312");
                            int b = resp.indexOf("title");
                            int eb = resp.indexOf("_个股体检_新浪网");
                            if (b != -1) {
                                String stock = resp.substring(b + 6, eb);
                                String stockNo = stock.substring(stock.length() - 7, stock.length() - 1);
                                String stockName = stock.substring(0, stock.length() - 8);
                                int t = resp.indexOf("机构介入比例");
                                String percent = resp.substring(t + 6, t + 10);
                                System.out.println("the percent" + percent);
                                StockA a = new StockA();
                                a.setStock_no(stockNo);
                                a.setStock_name(stockName);
                                a.setOrg_share(percent);
                                stocksResult.add(a);
                            } else {
                                File f1 = new File("c:\\wrongfile.txt");
                                FileWriter fw = new FileWriter(f1, true);
                                BufferedWriter bw = new BufferedWriter(fw);
                                bw.write(request.getRequestLine() + "->" + response.getStatusLine() + "\n");
                                bw.close();
                                fw.close();
                            }
                        } catch (ParseException e) {
                            System.out.println(request.getRequestLine() + "->" + response.getStatusLine());
                            e.printStackTrace();
                        } catch (IOException e) {
                            System.out.println(request.getRequestLine() + "->" + response.getStatusLine());
                            e.printStackTrace();
                        }
                        latch.countDown();
                        System.out.println(request.getRequestLine() + "->" + response.getStatusLine());
                    }

                    public void failed(final Exception ex) {
                        latch.countDown();
                        ex.printStackTrace();
                    }

                    public void cancelled() {
                        latch.countDown();
                    }
                });
            }
            latch.await();
            System.out.println("Shutting down");
        } finally {
            httpclient.shutdown();
        }
        System.out.println("Done");
        System.out.println(stocksResult.size());
        Collections.sort(stocksResult, new OrgShareComparator());
        Iterator<StockA> myresults = stocksResult.iterator();
        File f1 = new File("c:\\share_stat77.txt");
        FileWriter fw = new FileWriter(f1);
        BufferedWriter bw = new BufferedWriter(fw);
        int i = 1;
        while (myresults.hasNext()) {
            StockA stock = myresults.next();
            System.out.println(i + ": " + stock.getStock_no() + "(" + stock.getStock_name() + ")" + " : " + stock.getOrg_share());
            bw.write(i + ": " + stock.getStock_no() + "(" + stock.getStock_name() + ")" + " : " + stock.getOrg_share() + "\n");
            i++;
        }
        bw.close();
        fw.close();
        long t1 = System.currentTimeMillis();
        Time t = new Time(t1);
        System.out.println("End time " + t.toString());
    }
