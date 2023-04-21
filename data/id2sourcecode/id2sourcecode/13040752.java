    private void callServlet() {
        BufferedReader in = null;
        String inputLine;
        StringBuffer outputXML = new StringBuffer();
        boolean success = false;
        try {
            loadProperties();
            if (server == null || server.equals("") || fileName == null || fileName.equals("") || type == null || type.equals("") || method == null || method.equals("")) {
                this.printUsage();
                throw new Exception("Please specify all mandatory arguments.  See command line usage.");
            }
            trustCerts();
            URL url = new URL((server.equals("localhost") ? "http://" : "https://") + server + "/gnomex/login_verify.jsp?j_username=" + userName + "&j_password=" + password);
            URLConnection conn = url.openConnection();
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            success = false;
            while ((inputLine = in.readLine()) != null) {
                System.out.println(inputLine);
                if (inputLine.indexOf("<SUCCESS") >= 0) {
                    success = true;
                    break;
                }
            }
            if (!success) {
                System.err.print(outputXML.toString());
                throw new Exception("Unable to login");
            }
            List<String> cookies = conn.getHeaderFields().get("Set-Cookie");
            url = new URL((server.equals("localhost") ? "http://" : "https://") + server + "/gnomex/CreateSecurityAdvisor.gx");
            conn = url.openConnection();
            for (String cookie : cookies) {
                conn.addRequestProperty("Cookie", cookie.split(";", 2)[0]);
            }
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            success = false;
            outputXML = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                outputXML.append(inputLine);
                if (inputLine.indexOf("<SecurityAdvisor") >= 0) {
                    success = true;
                    break;
                }
            }
            if (!success) {
                System.err.print(outputXML.toString());
                throw new Exception("Unable to create security advisor");
            }
            String parms = URLEncoder.encode("fileName", "UTF-8") + "=" + URLEncoder.encode(fileName, "UTF-8");
            parms += "&" + URLEncoder.encode("transferType", "UTF-8") + "=" + URLEncoder.encode(type, "UTF-8");
            parms += "&" + URLEncoder.encode("transferMethod", "UTF-8") + "=" + URLEncoder.encode(method.toString(), "UTF-8");
            if (startDateTimeStr != null) {
                parms += "&" + URLEncoder.encode("startDateTime", "UTF-8") + "=" + URLEncoder.encode(startDateTimeStr.toString(), "UTF-8");
            }
            if (endDateTimeStr != null) {
                parms += "&" + URLEncoder.encode("endDateTime", "UTF-8") + "=" + URLEncoder.encode(endDateTimeStr.toString(), "UTF-8");
            }
            if (fileSize != null) {
                parms += "&" + URLEncoder.encode("fileSize", "UTF-8") + "=" + URLEncoder.encode(fileSize.toString(), "UTF-8");
            }
            success = false;
            outputXML = new StringBuffer();
            url = new URL((server.equals("localhost") ? "http://" : "https://") + server + "/gnomex/SaveTransferLog.gx");
            conn = url.openConnection();
            conn.setDoOutput(true);
            for (String cookie : cookies) {
                conn.addRequestProperty("Cookie", cookie.split(";", 2)[0]);
            }
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(parms);
            wr.flush();
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while ((inputLine = in.readLine()) != null) {
                System.out.print(inputLine);
                if (inputLine.indexOf("<SUCCESS") >= 0) {
                    success = true;
                }
            }
            System.out.println();
            if (!success) {
                throw new Exception("Unable to insert transfer log");
            }
        } catch (MalformedURLException e) {
            printUsage();
            e.printStackTrace();
            System.err.println(e.toString());
        } catch (IOException e) {
            printUsage();
            e.printStackTrace();
            System.err.println(e.toString());
        } catch (Exception e) {
            printUsage();
            e.printStackTrace();
            System.err.println(e.toString());
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }
    }
