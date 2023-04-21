    public Result book(String rangDate, String startDate, TrainQueryInfo train) {
        log.debug("-------------------book start-------------------");
        Result rs = new Result();
        HttpPost post = new HttpPost(Constants.BOOK_URL);
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("from_station_telecode", train.getFromStationCode()));
        formparams.add(new BasicNameValuePair("from_station_telecode_name", train.getFromStation()));
        formparams.add(new BasicNameValuePair("include_student", "00"));
        formparams.add(new BasicNameValuePair("lishi", "" + Util.getHour2Min(train.getTakeTime())));
        formparams.add(new BasicNameValuePair("round_start_time_str", rangDate));
        formparams.add(new BasicNameValuePair("round_train_date", Util.getCurDate()));
        formparams.add(new BasicNameValuePair("seattype_num", ""));
        formparams.add(new BasicNameValuePair("single_round_type", "1"));
        formparams.add(new BasicNameValuePair("start_time_str", rangDate));
        formparams.add(new BasicNameValuePair("station_train_code", train.getTrainCode()));
        formparams.add(new BasicNameValuePair("to_station_telecode", train.getToStationCode()));
        formparams.add(new BasicNameValuePair("to_station_telecode_name", train.getToStation()));
        formparams.add(new BasicNameValuePair("train_class_arr", "QB#D#Z#T#K#QT#"));
        formparams.add(new BasicNameValuePair("train_date", startDate));
        formparams.add(new BasicNameValuePair("train_pass_type", "QB"));
        formparams.add(new BasicNameValuePair("train_start_time", train.getStartTime()));
        BufferedReader br = null;
        try {
            UrlEncodedFormEntity uef = new UrlEncodedFormEntity(formparams, HTTP.UTF_8);
            post.setEntity(uef);
            HttpResponse response = httpclient.execute(post);
            HttpEntity entity = response.getEntity();
            log.debug(response.getStatusLine());
            br = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"));
            while ((br.readLine()) != null) {
            }
            rs.setState(Result.SUCC);
            rs.setMsg(response.getStatusLine().toString());
        } catch (Exception e) {
            log.error(e);
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        log.debug("-------------------book end-------------------");
        return rs;
    }
