	public static boolean uploadStatus(String token, String tokenSecret, File aFile, String status) {
		OAuthConsumer httpOAuthConsumer;
		httpOAuthConsumer = new DefaultOAuthConsumer(token, tokenSecret);
		httpOAuthConsumer.setTokenWithSecret(token, tokenSecret);
		boolean result = false;
		try {
			URL url = new URL("http://api.t.sina.com.cn/statuses/upload.json");
			HttpURLConnection request = (HttpURLConnection) url.openConnection();
			request.setDoOutput(true);
			request.setRequestMethod("POST");
			HttpParameters para = new HttpParameters();
			para.put("status", URLEncoder.encode(status, "utf-8").replaceAll("\\+", "%20"));
			String boundary = "---------------------------37531613912423";
			String content = "--" + boundary + "\r\nContent-Disposition: form-data; name=\"status\"\r\n\r\n";
			String pic = "\r\n--"
					+ boundary
					+ "\r\nContent-Disposition: form-data; name=\"pic\"; filename=\"image.jpg\"\r\nContent-Type: image/jpeg\r\n\r\n";
			byte[] end_data = ("\r\n--" + boundary + "--\r\n").getBytes();
			FileInputStream stream = new FileInputStream(aFile);
			byte[] file = new byte[(int) aFile.length()];
			stream.read(file);
			request.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary); // 设置表单类型和分隔符
			request.setRequestProperty(
					"Content-Length",
					String.valueOf(content.getBytes().length + status.getBytes().length + pic.getBytes().length
							+ aFile.length() + end_data.length)); // 设置内容长度
			httpOAuthConsumer.setAdditionalParameters(para);
			httpOAuthConsumer.sign(request);
			OutputStream ot = request.getOutputStream();
			ot.write(content.getBytes());
			ot.write(status.getBytes());
			ot.write(pic.getBytes());
			ot.write(file);
			ot.write(end_data);
			ot.flush();
			ot.close();
			request.connect();
			System.out.println(request.getResponseCode());
			if (200 == request.getResponseCode()) {

				result = true;
			} else {
				result = false;
			}

		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (OAuthMessageSignerException e) {
			e.printStackTrace();
		} catch (OAuthExpectationFailedException e) {
			e.printStackTrace();
		} catch (OAuthCommunicationException e) {
			e.printStackTrace();
		}
		return result;
	}
