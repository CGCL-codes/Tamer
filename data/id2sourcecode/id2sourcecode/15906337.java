	public static boolean shareToSina(String requestToken, String requestTokenSecrect, String status, String gpsLat,
			String gpsLong) {
		try {
			OAuthConsumer consumer = new DefaultOAuthConsumer(Cons.SINA_CONSUMER_KEY, Cons.SINA_CONSUMER_SECRET);
			consumer.setTokenWithSecret(requestToken, requestTokenSecrect);
			URL url = new URL("http://api.t.sina.com.cn/statuses/update.json");
			HttpURLConnection request = (HttpURLConnection) url.openConnection();
			request.setDoOutput(true);
			request.setRequestMethod("POST");
			HttpParameters para = new HttpParameters();
			para.put("status", URLEncoder.encode(status, "utf-8").replaceAll("\\+", "%20"));

			if (gpsLat != null && !"".equals(gpsLat)) {
				para.put("lat", URLEncoder.encode(gpsLat, "utf-8").replaceAll("\\+", "%20"));
			}

			if (gpsLong != null && !"".equals(gpsLong)) {
				para.put("long", URLEncoder.encode(gpsLong, "utf-8").replaceAll("\\+", "%20"));
			}

			consumer.setAdditionalParameters(para);
			consumer.sign(request);
			OutputStream ot = request.getOutputStream();
			ot.write(("status=" + URLEncoder.encode(status, "utf-8")).replaceAll("\\+", "%20").getBytes());
			if (gpsLat != null && !"".equals(gpsLat)) {
				ot.write(("&lat=" + URLEncoder.encode(gpsLat, "utf-8")).replaceAll("\\+", "%20").getBytes());
			}
			if (gpsLong != null && !"".equals(gpsLong)) {
				ot.write(("&long=" + URLEncoder.encode(gpsLong, "utf-8")).replaceAll("\\+", "%20").getBytes());
			}
			ot.flush();
			ot.close();
			request.connect();
			if (request.getResponseCode() == 200) {
				return true;
			}
		} catch (Exception e) {

		}
		return false;
	}
