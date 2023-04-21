    public static void send(String T, String ST) {
        String data;
        try {
            data = URLEncoder.encode("classe", "UTF-8") + "=" + URLEncoder.encode(T, "UTF-8");
            data += "&" + URLEncoder.encode("testo", "UTF-8") + "=" + URLEncoder.encode(ST, "UTF-8");
            URL url = new URL("http://" + Main.config.getRemoteRoot() + "/bug.asp?" + data);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            String str = "";
            while ((line = rd.readLine()) != null) {
                str += line;
            }
            rd.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
