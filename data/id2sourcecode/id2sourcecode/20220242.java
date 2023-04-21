    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        File dir = new File(getServletContext().getRealPath("."));
        String requestURL = request.getRequestURL().toString();
        int i = requestURL.indexOf(request.getContextPath() + "/") + 1;
        if (i == 0) {
            response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
            response.setHeader("Location", requestURL + "/");
            return;
        }
        int j = i + request.getContextPath().length();
        String fileName = requestURL.substring(j);
        if (fileName.equals("") || fileName.charAt(fileName.length() - 1) == '/') {
            if (new File(dir.getAbsolutePath() + "/index.xquery").exists()) fileName += "index.xquery";
            if (new File(dir.getAbsolutePath() + "/index.html").exists()) fileName += "index.html";
        }
        String filePath = dir.getAbsolutePath() + "/" + fileName;
        File queriedFile = new File(filePath);
        if (queriedFile.isDirectory()) {
            response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
            response.setHeader("Location", request.getContextPath() + "/" + fileName + "/");
            return;
        }
        if (!queriedFile.exists()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            PrintWriter print = response.getWriter();
            print.println("<html><body><H1>404 Not Found</H1>There is no query file named: " + filePath + "</body></html>");
            return;
        }
        try {
            if (queriedFile.getName().endsWith(".xquery")) {
                String query;
                query = FileReader.getFileContent(queriedFile.toURI().toString(), true);
                String queryResult = executeQuery(query, request, response);
                response.setContentType("text/html; charset=utf-8");
                PrintWriter print = response.getWriter();
                print.println(queryResult);
            } else if (queriedFile.getName().endsWith(".html")) {
                String html = FileReader.getFileContent(queriedFile.toURI().toString(), false);
                response.setContentType("text/html");
                PrintWriter print = response.getWriter();
                print.println(html);
            } else if (queriedFile.getName().endsWith(".css")) {
                response.setContentType("text/css");
                String css = FileReader.getFileContent(queriedFile.toURI().toString(), false);
                PrintWriter print = response.getWriter();
                print.println(css);
            } else if (queriedFile.getName().endsWith(".xml")) {
                String xml = FileReader.getFileContent(queriedFile.toURI().toString(), false);
                response.setContentType("text/xml; charset=utf-8");
                PrintWriter print = response.getWriter();
                print.println(xml);
            } else if (queriedFile.getName().endsWith(".js")) {
                String js = FileReader.getFileContent(queriedFile.toURI().toString(), false);
                response.setContentType("application/x-javascript; charset=utf-8");
                PrintWriter print = response.getWriter();
                print.println(js);
            } else if (queriedFile.getName().endsWith(".jpg")) {
                response.setContentType("image/jpg");
                OutputStream os = response.getOutputStream();
                InputStream is = new FileInputStream(queriedFile);
                int c = 0;
                while ((c = is.read()) != -1) os.write(c);
                os.close();
                is.close();
            } else if (queriedFile.getName().endsWith(".gif")) {
                response.setContentType("image/gif");
                OutputStream os = response.getOutputStream();
                InputStream is = new FileInputStream(queriedFile);
                int c = 0;
                while ((c = is.read()) != -1) os.write(c);
                os.close();
                is.close();
            } else if (queriedFile.getName().endsWith(".png")) {
                response.setContentType("image/png");
                OutputStream os = response.getOutputStream();
                InputStream is = new FileInputStream(queriedFile);
                int c = 0;
                while ((c = is.read()) != -1) os.write(c);
                os.close();
                is.close();
            }
        } catch (MXQueryException e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            PrintWriter print = response.getWriter();
            print.println("<html><body><H1>404 Not Found</H1>There is no query file named: " + filePath + "</body></html>");
            return;
        }
    }
