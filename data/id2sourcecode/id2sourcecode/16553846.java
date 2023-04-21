    @SuppressWarnings("deprecation")
    public void run() {
        while (true) {
            String urlstring = getUrl();
            if (urlstring == null) {
                Thread.currentThread().stop();
                return;
            }
            BufferedReader br = null;
            HttpURLConnection httpcon;
            StringBuffer sf = new StringBuffer();
            FileOutputStream fout;
            BufferedOutputStream bout;
            try {
                URL url = new URL(urlstring);
                httpcon = (HttpURLConnection) url.openConnection();
                br = new BufferedReader(new InputStreamReader(httpcon.getInputStream()));
                String temp = null;
                sf.append(urlstring + "\n");
                temp = br.readLine();
                while (temp != null) {
                    sf.append(temp);
                    String regex = "<a href=[\"]http://" + ".*?" + ">";
                    Pattern pt = Pattern.compile(regex);
                    Matcher mt = pt.matcher(temp);
                    if (mt.find()) {
                        temp = mt.group().replaceAll("<a href=|>", "");
                        temp = temp.substring(1);
                        int index = temp.indexOf("\"");
                        if (index > 0) {
                            temp = temp.substring(0, index);
                            addUrl(temp);
                        }
                    }
                    temp = br.readLine();
                }
                urlstring = urlstring.replaceAll("/|:|\\?|\\*|\"|<|>|\\|", ".");
                fout = new FileOutputStream(new File(savePath + urlstring.substring(7) + ".html"));
                bout = new BufferedOutputStream(fout);
                bout.write(sf.toString().getBytes());
                bout.flush();
                bout.close();
                fout.close();
                System.gc();
            } catch (ConnectException coe) {
                continue;
            } catch (Exception e) {
                continue;
            }
        }
    }
