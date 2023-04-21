    private void callServlet() {
        BufferedReader in = null;
        String inputLine;
        StringBuffer outputXML = new StringBuffer();
        boolean success = false;
        try {
            loadProperties();
            if (lab == null || name == null || name.equals("") || organism == null || genomeBuild == null || analysisType == null || folderName == null || folderName.equals("")) {
                this.printUsage();
                throw new Exception("Please specify all mandatory arguments.  See command line usage.");
            }
            trustCerts();
            URL url = new URL((server.equals("localhost") ? "http://" : "https://") + server + "/gnomex/login_verify.jsp?j_username=" + userName + "&j_password=" + password);
            URLConnection conn = url.openConnection();
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            success = false;
            while ((inputLine = in.readLine()) != null) {
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
            String parms = URLEncoder.encode("labName", "UTF-8") + "=" + URLEncoder.encode(lab, "UTF-8");
            parms += "&" + URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8");
            parms += "&" + URLEncoder.encode("newAnalysisGroupName", "UTF-8") + "=" + URLEncoder.encode(folderName, "UTF-8");
            parms += "&" + URLEncoder.encode("organism", "UTF-8") + "=" + URLEncoder.encode(organism, "UTF-8");
            parms += "&" + URLEncoder.encode("genomeBuild", "UTF-8") + "=" + URLEncoder.encode(genomeBuild, "UTF-8");
            parms += "&" + URLEncoder.encode("analysisType", "UTF-8") + "=" + URLEncoder.encode(analysisType, "UTF-8");
            parms += "&" + URLEncoder.encode("isBatchMode", "UTF-8") + "=" + URLEncoder.encode("Y", "UTF-8");
            if (description != null) {
                parms += "&" + URLEncoder.encode("description", "UTF-8") + "=" + URLEncoder.encode(description, "UTF-8");
            }
            if (folderDescription != null) {
                parms += "&" + URLEncoder.encode("newAnalysisGroupDescription", "UTF-8") + "=" + URLEncoder.encode(folderDescription, "UTF-8");
            }
            if (lanesXMLString != null) {
                parms += "&" + "lanesXMLString" + "=" + lanesXMLString;
            }
            success = false;
            outputXML = new StringBuffer();
            url = new URL((server.equals("localhost") ? "http://" : "https://") + server + "/gnomex/SaveAnalysis.gx");
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
                throw new Exception("Unable to create analysis");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            System.err.println(e.toString());
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println(e.toString());
        } catch (Exception e) {
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
