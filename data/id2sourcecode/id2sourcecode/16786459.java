    public static void check() throws IOException, GeneralSecurityException {
        System.out.println("Check ip ....");
        Date date = new Date(System.currentTimeMillis());
        DateFormat df = DateFormat.getDateInstance(DateFormat.FULL, Locale.ITALY);
        System.out.println("Date/Time Check: " + df.format(date));
        if (startParams.length < 3 && startParams.length == 0) {
            usage();
            return;
        }
        Hashtable<String, String> params = new Hashtable<String, String>();
        for (int i = 0; i < startParams.length; i++) {
            params.put(startParams[i], startParams[++i]);
        }
        if (params.get("-c") == null) {
            usage();
            return;
        }
        InputStream in = new FileInputStream(params.get("-c"));
        Properties p = new Properties();
        p.load(in);
        String sha = p.getProperty("sha-hash");
        if (sha == null) {
            if (params.get("-u") == null || params.get("-p") == null) {
                usage();
                return;
            }
            String user = params.get("-u");
            String pwd = params.get("-p");
            sha = user + "|" + pwd;
            byte[] b = MessageDigest.getInstance("SHA-1").digest(sha.getBytes());
            sha = hash(b);
            p.setProperty("sha-hash", sha);
            p.store(new FileOutputStream(params.get("-c"), true), "");
        }
        String oldIp = p.getProperty("ip");
        System.out.println("Ip Setted now: " + oldIp);
        String actualIp = getActualIp();
        System.out.println("NEW IP: " + actualIp);
        if (!actualIp.equals(oldIp)) {
            URL_DNS += p.getProperty("sha-hash");
            URL url = new URL(URL_DNS);
            URLConnection urlConnection = url.openConnection();
            InputStream inStream = urlConnection.getInputStream();
            StringBuffer sb = streamToStringBuffer(inStream);
            StringTokenizer st = new StringTokenizer(sb.toString(), "|");
            Vector<String> v = new Vector<String>();
            while (st.hasMoreTokens()) {
                String token = st.nextToken();
                v.add(token);
            }
            String url4change = v.get(2);
            url = new URL(url4change);
            urlConnection = url.openConnection();
            inStream = urlConnection.getInputStream();
            System.out.println(streamToStringBuffer(inStream));
        }
        p.setProperty("ip", actualIp);
        p.store(new FileOutputStream(params.get("-c")), "");
    }
