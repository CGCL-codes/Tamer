    public static Train doLoadTrainFromWeb(String code, String proxyAddress, int proxyPort) {
        Train train = new Train();
        train.trainNameFull = "";
        Date now = new Date();
        SimpleDateFormat format = new SimpleDateFormat("MM");
        String mon = format.format(now);
        format = new SimpleDateFormat("dd");
        String day = format.format(now);
        String postData = "nmonth1=" + mon + "&nmonth1_new_value=true&nday1=" + day + "&nday1_new_value=true&trainCode=" + code + "&trainCode_new_value=true";
        try {
            Proxy proxy = null;
            if (proxyAddress.equals("") || proxyPort == 0) proxy = Proxy.NO_PROXY; else proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyAddress, proxyPort));
            URL url = new URL("http://dynamic.12306.cn/TrainQuery/iframeTrainPassStationByTrainCode.jsp");
            URLConnection conn = url.openConnection(proxy);
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
            conn.setRequestProperty("Referer", "www.12306.cn");
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(postData);
            wr.flush();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                if ((inputLine.indexOf("//") == -1) && (inputLine.indexOf("parent.mygrid.addRow") != -1)) {
                    String[] items = inputLine.split(",");
                    if (items[4].indexOf("----") != -1) items[4] = items[5];
                    if (items[5].indexOf("----") != -1) items[5] = items[4];
                    Stop stop = new Stop(items[2].split("\\^")[0].trim(), items[4].trim(), items[5].trim(), true);
                    train.appendStop(stop);
                    String c = items[3].trim();
                    if ((c.charAt(c.length() - 1) - '0') % 2 == 0) {
                        if (train.trainNameUp.equals("")) train.trainNameUp = c;
                    } else {
                        if (train.trainNameDown.equals("")) train.trainNameDown = c;
                    }
                    if (train.trainNameFull.indexOf(c) == -1) {
                        if (train.trainNameFull.equals("")) {
                            train.trainNameFull = c;
                        } else {
                            train.trainNameFull += ("/" + c);
                        }
                    }
                }
            }
            in.close();
            String[] names = train.trainNameFull.split("/");
            for (int i = 0; i < names.length; ++i) {
                if ((names[i].charAt(names[i].length() - 1) - '0') % 2 == 0) {
                    train.trainNameUp = names[i];
                } else {
                    train.trainNameDown = names[i];
                }
            }
            return train;
        } catch (Exception e) {
            return null;
        }
    }
