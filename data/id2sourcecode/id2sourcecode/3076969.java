    public static String getData(String urlStr) throws FileNotFoundException, UnknownHostException, IOException {
        String result = null;
        try {
            URL url = new URL(urlStr);
            URLConnection conn = url.openConnection();
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            result = sb.toString();
        } catch (UnknownHostException e) {
            throw e;
        } catch (FileNotFoundException e) {
            throw e;
        } catch (NoRouteToHostException e) {
            throw e;
        } catch (SocketException e) {
            throw new UnknownHostException();
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
