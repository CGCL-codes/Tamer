    private InputSource sendRequest(String f) {
        InputSource iSource = null;
        try {
            String serviceEndPoint = Navigator.getPreferences().getOpenLSDirectoryServiceEndPoint();
            if (Navigator.isVerbose()) {
                System.out.println("sending request to " + serviceEndPoint);
                System.out.println(f);
            }
            URL u = new URL(serviceEndPoint);
            HttpURLConnection urlc = (HttpURLConnection) u.openConnection();
            urlc.setReadTimeout(Navigator.TIME_OUT);
            urlc.setAllowUserInteraction(false);
            urlc.setRequestMethod("POST");
            urlc.setRequestProperty("Content-Type", "application/xml");
            urlc.setDoOutput(true);
            urlc.setDoInput(true);
            urlc.setUseCaches(false);
            PrintWriter xmlOut = null;
            xmlOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(urlc.getOutputStream())));
            xmlOut = new java.io.PrintWriter(urlc.getOutputStream());
            xmlOut.write(f);
            xmlOut.flush();
            xmlOut.close();
            is = urlc.getInputStream();
            response = "";
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String line;
            while ((line = rd.readLine()) != null) {
                response = response + line;
            }
            is.close();
            System.out.println(response);
            StringReader sReader = new StringReader(response);
            iSource = new InputSource(sReader);
        } catch (Exception e) {
            remove(bottomPanel);
            bottomPanel = errorPanel();
            setDividerLocation(dividerLocation);
            add(bottomPanel);
            e.printStackTrace();
        }
        return iSource;
    }
