    public String postRequest() {
        error = false;
        errorStr = "";
        isResponseError = false;
        String responseString = null;
        String requestString = new String("");
        try {
            for (java.util.Iterator i = parameters.entrySet().iterator(); i.hasNext(); ) {
                java.util.Map.Entry e = (java.util.Map.Entry) i.next();
                requestString = requestString + URLEncoder.encode((String) e.getKey(), "UTF-8") + "=" + URLEncoder.encode((String) e.getValue(), "UTF-8") + "&";
            }
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();
            HttpURLConnection httpConn = (HttpURLConnection) connection;
            httpConn.setRequestProperty("Content-Length", String.valueOf(requestString.length()));
            httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpConn.setRequestMethod("POST");
            httpConn.setDoOutput(true);
            httpConn.setDoInput(true);
            connection.connect();
            PrintWriter out = new PrintWriter(httpConn.getOutputStream());
            out.println(requestString);
            out.close();
            InputStreamReader isr = new InputStreamReader(httpConn.getInputStream());
            BufferedReader br = new BufferedReader(isr);
            String temp;
            String tempResponse = "";
            while ((temp = br.readLine()) != null) tempResponse = tempResponse + temp + "\n";
            responseString = tempResponse;
            checkResponseError(responseString);
            br.close();
            isr.close();
            httpConn.disconnect();
        } catch (java.net.ConnectException conne) {
            error = true;
            finished = true;
            errorStr = "Cannot connect to: " + urlString + "\n" + "                     Server is not responding.";
        } catch (java.io.InterruptedIOException e) {
            error = true;
            finished = true;
            errorStr = "Connection to Portal lost: communication is timeouted.";
            this.parentWorkflow.getMenuButtonEventHandler().stopAutomaticRefresh();
        } catch (java.net.MalformedURLException e) {
            error = true;
            finished = true;
            errorStr = "Error in communication: " + e.getMessage();
            System.out.println("postRequest()-MalformedURLException.");
            e.printStackTrace();
        } catch (Exception e) {
            error = true;
            finished = true;
            errorStr = "Error while trying to communicate the server: " + e.getMessage();
        }
        return responseString;
    }
