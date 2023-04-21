    protected HttpURLConnection getConnection(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        StringBuilder agentBuilder = new StringBuilder();
        agentBuilder.append(cxt.getString(R.string.app_name)).append(' ').append(cxt.getString(R.string.app_version)).append('|').append(Build.DISPLAY).append('|').append(VERSION.RELEASE).append('|').append(Build.ID).append('|').append(Build.MODEL).append('|').append(Locale.getDefault().getLanguage()).append('-').append(Locale.getDefault().getCountry());
        connection.setRequestProperty("User-Agent", agentBuilder.toString());
        return connection;
    }
