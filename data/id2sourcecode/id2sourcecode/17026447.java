    public void GetWeekWeather(Context context) {
        parser = new Parser();
        ArrayList<WeatherForecast> temp = parser.connectWeather(context);
        SetTodayWeather();
        wl_l.setVisibility(View.VISIBLE);
        day1.setText(temp.get(0).dayOfWeek);
        day2.setText(temp.get(1).dayOfWeek);
        day3.setText(temp.get(2).dayOfWeek);
        day4.setText(temp.get(3).dayOfWeek);
        dayH1.setText(temp.get(0).high + "˚C");
        dayH2.setText(temp.get(1).high + "˚C");
        dayH3.setText(temp.get(2).high + "˚C");
        dayH4.setText(temp.get(3).high + "˚C");
        dayL1.setText(temp.get(0).low + "˚C");
        dayL2.setText(temp.get(1).low + "˚C");
        dayL3.setText(temp.get(2).low + "˚C");
        dayL4.setText(temp.get(3).low + "˚C");
        dayC1.setText(temp.get(0).condition);
        dayC2.setText(temp.get(1).condition);
        dayC3.setText(temp.get(2).condition);
        dayC4.setText(temp.get(3).condition);
        try {
            Bitmap bm;
            URL url = new URL("http://www.google.com" + temp.get(0).icon);
            URLConnection connection = url.openConnection();
            connection.connect();
            InputStream is = connection.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            dayImg1.setImageBitmap(bm);
            url = new URL("http://www.google.com" + temp.get(1).icon);
            connection = url.openConnection();
            connection.connect();
            is = connection.getInputStream();
            bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            dayImg2.setImageBitmap(bm);
            url = new URL("http://www.google.com" + temp.get(2).icon);
            connection = url.openConnection();
            connection.connect();
            is = connection.getInputStream();
            bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            dayImg3.setImageBitmap(bm);
            url = new URL("http://www.google.com" + temp.get(3).icon);
            connection = url.openConnection();
            connection.connect();
            is = connection.getInputStream();
            bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            dayImg4.setImageBitmap(bm);
            bis.close();
            is.close();
        } catch (IOException e) {
        }
    }
