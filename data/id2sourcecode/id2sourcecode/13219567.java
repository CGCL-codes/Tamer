        protected Message doInBackground(String... parameters) {
            String photo = parameters[0];
            String shout = parameters[1];
            String checkinPrivate = parameters[2];
            String twitter = parameters[3];
            String facebook = parameters[4];
            Bundle data = new Bundle();
            String result;
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("vid", venueId));
            params.add(new BasicNameValuePair("shout", shout));
            params.add(new BasicNameValuePair("user", username));
            params.add(new BasicNameValuePair("password", password));
            params.add(new BasicNameValuePair("private", checkinPrivate));
            params.add(new BasicNameValuePair("twitter", twitter));
            params.add(new BasicNameValuePair("facebook", facebook));
            if (Common.HIGHDEBUG) {
                Log.d(Common.TAG, "Checkin private:" + params.get(4).getValue());
                Log.d(Common.TAG, "Checkin twitter:" + params.get(5).getValue());
                Log.d(Common.TAG, "Checkin facebook:" + params.get(6).getValue());
            }
            String postURL = Common.getBaseUrl() + "/upload";
            Log.d(Common.TAG, "URL : " + postURL);
            DefaultHttpClient threadHttpClient = activity.httpclient;
            if (photo != null) {
                long MAX_UPLOAD_SIZE = 300 * 1024;
                File file = new File(photo);
                long imageSize = file.length();
                if (Common.HIGHDEBUG) {
                    Log.d(Common.TAG, "photo : " + photo);
                    Log.d(Common.TAG, "Size : " + imageSize);
                }
                ContentBody bin = null;
                if (imageSize <= MAX_UPLOAD_SIZE) {
                    if (Common.HIGHDEBUG) Log.d(Common.TAG, "Photo is small enough. No resize.");
                    bin = new FileBody(file);
                } else {
                    byte[] image = Common.getLittlePicture(photo, CheckinToVenue.this);
                    bin = new InputStreamKnownSizeBody(new ByteArrayInputStream(image), image.length, "image/jpeg", photo);
                }
                try {
                    HttpPost post = new HttpPost(postURL);
                    MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                    reqEntity.addPart("myPicture", bin);
                    post.setEntity(reqEntity);
                    for (int i = 0; i < params.size(); i++) {
                        StringBody sb = new StringBody(params.get(i).getValue());
                        reqEntity.addPart(params.get(i).getName(), sb);
                    }
                    HttpResponse response = threadHttpClient.execute(post);
                    HttpEntity resEntity = response.getEntity();
                    if (resEntity != null) {
                        result = EntityUtils.toString(resEntity);
                        data.putString("checkinResult", result);
                    }
                } catch (Exception e) {
                    data.putString("eMessage", e.getMessage());
                    if (Common.HIGHDEBUG) {
                        Log.e(Common.TAG, "Exception uploadImage: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            } else {
                try {
                    result = Common.postUrlData(threadHttpClient, postURL, params);
                    data.putString("checkinResult", result);
                } catch (IOException e) {
                    data.putString("eMessage", e.getMessage());
                }
            }
            if (Common.HIGHDEBUG) Log.d(Common.TAG, "serverCheckin : " + data.getString("checkinResult"));
            msg.setData(data);
            return msg;
        }
