    private boolean postData(Match match) {
        progressDialog.setProgress(5);
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(URI_UPLOAD_MATCH);
        boolean result = false;
        try {
            UrlEncodedFormEntity en = new UrlEncodedFormEntity(getMatchPostData(match));
            httppost.setEntity(en);
            HttpResponse response = httpclient.execute(httppost);
            progressDialog.setProgress(10);
            HttpEntity he = response.getEntity();
            BufferedReader in = new BufferedReader(new InputStreamReader(he.getContent()));
            String responseLine = in.readLine();
            Log.i(LOG_TAG, responseLine);
            String[] r = responseLine.trim().split(";");
            if (r.length == 2) {
                String matchId = r[0];
                int frameId = Integer.parseInt(r[1]);
                int frames = match.getFrames().size() - frameId;
                int i = 0;
                for (Frame f : match.getFrames()) {
                    if (f.getFrameId() <= frameId) {
                        continue;
                    } else {
                        HttpPost fhttppost = new HttpPost(URI_UPLOAD_FRAME);
                        fhttppost.setEntity(new UrlEncodedFormEntity(getFramePostData(f, matchId, match.getPlayer1Name(), match.getPlayer2Name())));
                        response = httpclient.execute(fhttppost);
                        he = response.getEntity();
                        in = new BufferedReader(new InputStreamReader(he.getContent()));
                        while ((responseLine = in.readLine()) != null) {
                            Log.i(LOG_TAG, responseLine);
                        }
                        i++;
                        double p = (double) i * (double) 90 / (double) frames;
                        progressDialog.setProgress(10 + (int) p);
                    }
                }
                result = true;
            } else {
                result = false;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
