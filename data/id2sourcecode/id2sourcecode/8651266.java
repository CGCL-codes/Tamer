    @Override
    public void run() {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpPost httpost = new HttpPost(url);
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        for (int i = 0; i < sendData.length; i++) {
            if (sendData[i].split("=").length < 2) continue;
            nvps.add(new BasicNameValuePair(sendData[i].split("=")[0], sendData[i].split("=")[1]));
        }
        StringEntity myen;
        try {
            myen = new UrlEncodedFormEntity(nvps, encode);
            httpost.setEntity(myen);
            httpost.setHeader("referer", referer);
            HttpResponse response = httpclient.execute(httpost);
            HttpEntity entity = response.getEntity();
            String out = EntityUtils.toString(entity);
            if (encode.toLowerCase() == "null" || encode.equalsIgnoreCase("utf-8")) htmlData = out; else htmlData = new String(out.getBytes(HTTP.ISO_8859_1), encode);
            if (entity != null) {
                entity.consumeContent();
            }
        } catch (UnsupportedEncodingException e) {
            cn.imgdpu.util.CatException.getMethod().catException(e, "不支持的编码类型");
        } catch (ParseException e) {
            cn.imgdpu.util.CatException.getMethod().catException(e, "未知异常");
        } catch (java.net.ConnectException e) {
            cn.imgdpu.util.CatException.getMethod().catException(e, "网络连接超时");
        } catch (IOException e) {
            cn.imgdpu.util.CatException.getMethod().catException(e, "IO异常");
        }
    }
