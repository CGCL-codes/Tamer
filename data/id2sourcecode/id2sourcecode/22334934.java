    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String urlString = request.getParameter(RequestConst.URL_PARAMETER);
        ServletOutputStream out = response.getOutputStream();
        response.setContentType("text/plain");
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (IOException e) {
            out.println("Error: '" + urlString + "' is not a valid URL.");
            return;
        }
        InputStream in = null;
        try {
            URLConnection connection = url.openConnection();
            connection.setAllowUserInteraction(false);
            in = connection.getInputStream();
        } catch (IOException e) {
            out.println("Error: Unable to connect to '" + urlString + "'.");
            return;
        }
        try {
            MemoryReuseableDocumentStorage storage = new MemoryReuseableDocumentStorage(new FilterProperties(), new EmptyErrorHandler());
            storage.store(in);
            BufferedReader reader = new BufferedReader(storage.getReader());
            String line = "";
            int lineNumber = 0;
            while (true) {
                lineNumber++;
                line = reader.readLine();
                if (line == null) break;
                out.println(lineNumber + " \t " + line);
            }
        } catch (Exception e) {
            e.printStackTrace();
            out.println("Error: '" + e.getMessage() + "'.");
        }
        return;
    }
