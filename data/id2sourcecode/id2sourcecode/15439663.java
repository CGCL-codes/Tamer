    public ArrayList<HashMap<String, Object>> getLotteryData(int lotteryType) {
        ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        try {
            HttpGet httpGet = new HttpGet(generateUrl(lotteryType));
            HttpResponse httpResponse = new DefaultHttpClient().execute(httpGet);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                InputStream inputStream = httpResponse.getEntity().getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "GBK"));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    if (line.charAt(0) <= '9' && line.charAt(0) >= '0') {
                        list.add(parseTxt(line));
                    }
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (StringIndexOutOfBoundsException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
