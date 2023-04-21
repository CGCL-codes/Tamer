    public Reader submitForm(ActualFormParameters params, Hashtable properties) throws MalformedURLException, IOException {
        InputStream resultStream;
        URL url;
        if (getMethod() == POST) url = getActionURL(); else url = new URL(getActionURL().toString() + "?" + params.toString());
        URLConnection c = url.openConnection();
        for (Enumeration e = properties.keys(); e.hasMoreElements(); ) {
            Object k = e.nextElement();
            Object v = properties.get(k);
            c.setRequestProperty(k.toString(), v.toString());
        }
        if (getMethod() == POST) {
            c.setDoOutput(true);
            c.setDoInput(true);
            OutputStream os = c.getOutputStream();
            c.connect();
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(os));
            pw.print(params.toString());
            pw.flush();
            pw.close();
            resultStream = c.getInputStream();
        } else resultStream = c.getInputStream();
        return new BufferedReader(new InputStreamReader(resultStream));
    }
