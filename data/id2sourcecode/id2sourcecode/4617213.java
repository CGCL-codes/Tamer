    private String postRequest2() {
        error = false;
        errorStr = "";
        String emptyStr = "";
        isResponseError = false;
        String responseString = null;
        String requestString = new String("");
        this.isThreadStopped = false;
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
            if (isThreadStopped) return emptyStr;
            connection.connect();
            System.out.println("Done.");
            if (isThreadStopped) return emptyStr;
            PrintWriter out = new PrintWriter(httpConn.getOutputStream());
            if (isThreadStopped) return emptyStr;
            out.println(requestString);
            out.close();
            if (isThreadStopped) return emptyStr;
            InputStreamReader isr = new InputStreamReader(httpConn.getInputStream());
            BufferedReader br = new BufferedReader(isr);
            String temp;
            String tempResponse = "";
            if (isThreadStopped) return emptyStr;
            while ((temp = br.readLine()) != null) tempResponse = tempResponse + temp + "\n";
            System.out.println("HTTPCommunication.postRequest2()-Reading response finished.");
            if (isThreadStopped) return emptyStr;
            responseString = tempResponse;
            br.close();
            isr.close();
            httpConn.disconnect();
        } catch (java.net.ConnectException conne) {
            error = true;
            finished = true;
            errorStr = "Cannot connect to: " + urlString + "\n" + "                     Server is not responding.";
            if (isThreadStopped) return emptyStr;
        } catch (java.io.InterruptedIOException e) {
            error = true;
            finished = true;
            errorStr = "Connection to Portal lost: communication is timeouted.";
            this.parentWorkflow.getMenuButtonEventHandler().stopAutomaticRefresh();
            if (isThreadStopped) return emptyStr;
        } catch (java.net.MalformedURLException e) {
            error = true;
            finished = true;
            errorStr = "Error in communication: " + e.getMessage();
            System.out.println("postRequest()-MalformedURLException:" + e);
            if (isThreadStopped) return emptyStr;
        } catch (Exception e) {
            error = true;
            finished = true;
            errorStr = "Error while trying to communicate the server: " + e.getMessage();
            System.out.println("postRequest()-Exception." + e);
            if (isThreadStopped) return emptyStr;
        }
        return responseString;
    }
