    private void makeRequest(String buff) {
        DataOutputStream printout = null;
        URL url = getUrl();
        if (url == null) return;
        try {
            URLConnection urlConn = url.openConnection();
            urlConn.setDoOutput(true);
            urlConn.setUseCaches(false);
            urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            printout = new DataOutputStream(urlConn.getOutputStream());
            String content = "uuid=" + URLEncoder.encode(Activator.getUUIDStr(), "UTF-8") + "&contents=" + URLEncoder.encode(buff, "UTF-8");
            printout.writeBytes(content);
            printout.flush();
            printout.close();
            DataInputStream input = new DataInputStream(urlConn.getInputStream());
            while (null != input.readLine()) ;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
    }
