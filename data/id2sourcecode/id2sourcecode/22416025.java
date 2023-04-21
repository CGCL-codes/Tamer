    protected void handleResult(TargetResult result, HttpServletResponse response) throws IOException {
        if ((result.getStatus() == HttpServletResponse.SC_MOVED_TEMPORARILY || result.getStatus() == HttpServletResponse.SC_MOVED_PERMANENTLY)) {
            URL url = new URL(result.getHeaders().get("Location"));
            response.setContentType("text/html");
            ServletOutputStream out = response.getOutputStream();
            out.println("<html>");
            out.println("<head>");
            out.println("</head>");
            out.println("<body>");
            out.println("<center>Automatic redirect to: <a href=\"" + url.getPath() + "\">" + url.getPath() + "</a></center>");
            out.println("<center>" + result.getHeaders().get("Location") + "</center>");
            out.println("</body>");
            out.println("</html>");
        } else {
            response.setStatus(result.getStatus());
            for (Map.Entry<String, String> entry : result.getHeaders().entrySet()) {
                response.setHeader(entry.getKey(), entry.getValue());
            }
            InputStream in = result.getContent();
            OutputStream out = response.getOutputStream();
            byte[] buffer = new byte[8192];
            int readed;
            while ((readed = in.read(buffer)) > 0) {
                out.write(buffer, 0, readed);
            }
            in.close();
            out.flush();
            out.close();
        }
    }
