    public static void main(String[] args) throws Exception {
        System.out.println("开始运行");
        File f = new File("stock4.properties");
        FileReader fr = new FileReader(f);
        BufferedReader br = new BufferedReader(fr);
        ArrayList<String> al = new ArrayList<String>();
        String s;
        while ((s = br.readLine()) != null) {
            String[] s1 = s.split(",");
            al.add("http://finance.sina.com.cn/realstock/company/" + s1[1] + s1[0] + "/nc.shtml");
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
                            int eb = resp.indexOf("_沪深行情");
                            if (b != -1) {
                                String stock = resp.substring(b + 6, eb);
                                String stockNo = stock.substring(stock.length() - 7, stock.length() - 1);
                                String stockName = stock.substring(0, stock.length() - 8);
                                int t = resp.indexOf("增减状况");
                                StockA a = new StockA();
                                if (t != -1) {
                                    String now = resp.substring(t + 25);
                                    for (int i = 0; i < 10; i++) {
                                        ShareHolder sh = new ShareHolder();
                                        for (int j = 0; j < 4; j++) {
                                            int f = now.indexOf("td");
                                            String elem = "no";
                                            if (f > 0) {
                                                String next = now.substring(f);
                                                int g = next.indexOf(">");
                                                String cur = next.substring(g);
                                                int h = cur.indexOf("<");
                                                if (h > 1) {
                                                    elem = cur.substring(1, h);
                                                    now = cur.substring(6 + h);
                                                }
                                            }
                                            if (j % 4 == 0) {
                                                sh.setName(elem);
                                            } else if (j % 4 == 1) {
                                                sh.setHolderAmount(elem);
                                            } else if (j % 4 == 2) {
                                                sh.setOperation(elem);
                                            } else if (j % 4 == 3) {
                                                sh.setDelta(elem);
                                            }
                                        }
                                        a.getShareHolders().add(sh);
                                    }
                                }
                                a.setStock_no(stockNo);
                                a.setStock_name(stockName);
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
        Collections.sort(stocksResult, new OrgEnterComparator());
        Iterator<StockA> myresults = stocksResult.iterator();
        File f1 = new File("c:\\share_stat88.txt");
        FileWriter fw = new FileWriter(f1);
        BufferedWriter bw = new BufferedWriter(fw);
        int i = 1;
        while (myresults.hasNext()) {
            StockA stock = myresults.next();
            System.out.println(i + ": " + stock.getStock_no() + "(" + stock.getStock_name() + ")" + " : " + StockUtils.countOrg(stock));
            bw.write(i + ": " + stock.getStock_no() + "(" + stock.getStock_name() + ")" + " : " + StockUtils.countOrg(stock) + "\n");
            i++;
        }
        bw.close();
        fw.close();
        long t1 = System.currentTimeMillis();
        Time t = new Time(t1);
        System.out.println("End time " + t.toString());
    }
