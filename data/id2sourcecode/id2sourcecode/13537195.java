    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String t = "http://localhost:8080/access/content/group/81c8542d-3f58-48cf-ac72-9f482df47ebe/sss/QuestionarioSocioEc.pdf";
        URL url = new URL(t);
        HttpURLConnection srvletConnection = (HttpURLConnection) url.openConnection();
        srvletConnection.setDoOutput(true);
        srvletConnection.setRequestMethod("POST");
        srvletConnection.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
        String myCookie = "JSESSIONID=e5bdde47-e638-46b7-8a00-12f056fa09ad.localhost";
        srvletConnection.setRequestProperty("Cookie", myCookie);
        srvletConnection.setInstanceFollowRedirects(true);
        srvletConnection.connect();
        DataOutputStream out2 = new DataOutputStream(srvletConnection.getOutputStream());
        out2.flush();
        out2.close();
        for (int i = 1; (headerName = srvletConnection.getHeaderFieldKey(i)) != null; i++) {
            if (headerName.equals("Set-Cookie")) {
                String cookie = srvletConnection.getHeaderField(i);
                System.out.println("COOKIES - " + cookie);
            }
            InputStream inp = srvletConnection.getInputStream();
            PrintWriter out = response.getWriter();
            BufferedReader rd = new BufferedReader(new InputStreamReader(inp));
            String line;
            while ((line = rd.readLine()) != null) {
                System.out.println(line);
                out.write(line);
            }
            System.out.println(srvletConnection.getURL());
            inp.close();
            out.close();
        }
    }
