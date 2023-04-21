    public void SetTodayWeather() {
        TodayCondition.setText(wn.condition);
        TodayC.setText(wn.temp_c + "˚C");
        TodayF.setText(wn.temp_f + "˚F");
        TodayHumidity.setText(wn.humidity);
        TodayWind.setText(wn.wind);
        try {
            Bitmap bm;
            URL url = new URL("http://www.google.com" + wn.icon);
            URLConnection connection = url.openConnection();
            connection.connect();
            InputStream is = connection.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            TodayIcon.setImageBitmap(bm);
            bis.close();
            is.close();
        } catch (IOException e) {
        }
    }
