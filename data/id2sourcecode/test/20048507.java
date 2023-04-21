    protected void parse(Attendance attendance) {
        this.attendance = attendance;
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", "Firefox/2.0");
            connection.connect();
            final InputStream is = connection.getInputStream();
            try {
                reader = new LineNumberReader(new BufferedReader(new InputStreamReader(is)));
                readLine();
                if (skipToStart()) {
                    while (hasLine()) {
                        if (!parseNext()) {
                            break;
                        }
                    }
                }
            } finally {
                is.close();
                connection.disconnect();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
