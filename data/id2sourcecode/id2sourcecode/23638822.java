    public static void rateSerie(int id, int value, Context context) {
        URL url;
        String site = context.getString(R.string.rateEpisodeUrl);
        String rateParam = context.getString(R.string.rateParam);
        String idParam = context.getString(R.string.idParam);
        try {
            url = new URL(site + "?" + rateParam + "=" + value + "&" + idParam + "=" + id);
            System.out.println("URL: " + url);
            URLConnection connection;
            connection = url.openConnection();
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            int responseCode;
            responseCode = httpConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println("Votado");
            } else {
                System.out.println("Error en la votacion");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
