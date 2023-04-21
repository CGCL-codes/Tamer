	public static boolean shareToSinaWithFile(String requestToken, String requestTokenSecrect, String blogContent,
			String gpsLat, String gpsLong, String picture) {
		try {
			String glat = "";
			String Gpslat = "";
			String glong = "";
			String Gpslong = "";
			OAuthConsumer consumer = new DefaultOAuthConsumer(Cons.SINA_CONSUMER_KEY, Cons.SINA_CONSUMER_SECRET);
			consumer.setTokenWithSecret(requestToken, requestTokenSecrect);
			URL url = new URL("http://api.t.sina.com.cn/statuses/upload.json");
			HttpURLConnection request = (HttpURLConnection) url.openConnection();
			request.setDoOutput(true);
			request.setRequestMethod("POST");
			HttpParameters para = new HttpParameters();
			// String status =
			// URLEncoder.encode(blogContent,"utf-8").replaceAll("\\+", "%20");
			String status = blogContent;
			if (gpsLong != null && !"".equals(gpsLong)) {
				Gpslong = URLEncoder.encode(gpsLong, "utf-8").replaceAll("\\+", "%20");
			}
			if (gpsLat != null && !"".equals(gpsLat)) {
				Gpslat = URLEncoder.encode(gpsLat, "utf-8").replaceAll("\\+", "%20");
			}
			para.put("status", URLEncoder.encode(blogContent, "utf-8").replaceAll("\\+", "%20"));
			if (gpsLat != null && !"".equals(gpsLat)) {
				para.put("lat", URLEncoder.encode(gpsLat, "utf-8").replaceAll("\\+", "%20"));
			}
			if (gpsLong != null && !"".equals(gpsLong)) {
				para.put("long", URLEncoder.encode(gpsLong, "utf-8").replaceAll("\\+", "%20"));
			}
			String boundary = "---------------------------37531613912423";
			String content = "--" + boundary + "\r\nContent-Disposition: form-data; name=\"status\"\r\n\r\n";
			if (gpsLat != null && !"".equals(gpsLat)) {
				glat = "\r\n--" + boundary + "\r\nContent-Disposition: form-data; name=\"lat\"\r\n\r\n";
			}
			if (gpsLong != null && !"".equals(gpsLong)) {
				glong = "\r\n--" + boundary + "\r\nContent-Disposition: form-data; name=\"long\"\r\n\r\n";
			}
			String pic = "\r\n--"
					+ boundary
					+ "\r\nContent-Disposition: form-data; name=\"pic\"; filename=\"icon.png\"\r\nContent-Type: image/jpeg\r\n\r\n";
			byte[] end_data = ("\r\n--" + boundary + "--\r\n").getBytes();
			File f = new File(picture);
			FileInputStream stream = new FileInputStream(f);
			byte[] file = new byte[(int) f.length()];
			stream.read(file);
			request.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary); // 设置表单类型和分隔符
			request.setRequestProperty(
					"Content-Length",
					String.valueOf(content.getBytes().length + status.getBytes().length + glat.getBytes().length
							+ Gpslat.getBytes().length + glong.getBytes().length + Gpslong.getBytes().length
							+ pic.getBytes().length + f.length() + end_data.length)); // 设置内容长度

			consumer.setAdditionalParameters(para);
			consumer.sign(request);
			OutputStream ot = request.getOutputStream();
			ot.write(content.getBytes());
			ot.write(status.getBytes());
			if (gpsLat != null && !"".equals(gpsLat)) {
				ot.write(glat.getBytes());
				ot.write((Gpslat).getBytes());
			}
			if (gpsLong != null && !"".equals(gpsLong)) {
				ot.write(glong.getBytes());
				ot.write((Gpslong).getBytes());
			}
			ot.write(pic.getBytes());
			ot.write(file);
			ot.write(end_data);
			ot.flush();
			ot.close();
			request.connect();
			if (request.getResponseCode() == 200) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
