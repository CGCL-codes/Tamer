    @Override
    public String getContent(final URL url) {
        String ret = null;
        try {
            final URLConnection conn = url.openConnection();
            final BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF8"));
            final StringBuffer response = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            ret = response.toString();
        } catch (final SocketTimeoutException e) {
        } catch (final Exception e) {
        }
        return ret;
    }
