    private void sendMessageByHTTPs(String msg, URL url) {
        try {
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            DataOutputStream out = new DataOutputStream(conn.getOutputStream());
            String name = PreferenceManager.getDefault().get("login");
            out.writeBytes("requestor=" + URLEncoder.encode(name, "UTF-8"));
            out.writeBytes("&content=" + URLEncoder.encode(msg, "UTF-8"));
            out.writeBytes(msg);
            out.flush();
            out.close();
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line).append("\n");
            }
            conn.getInputStream().close();
            Notifier.processLog(result);
        } catch (IOException ex) {
            Logger.getLogger(ConnectionManager.class.getName()).log(Level.SEVERE, null, ex);
            Notifier.sendException(ex);
        }
    }
