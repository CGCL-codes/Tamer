        public void respondGet(HttpServletResponse resp) throws IOException {
            setHeaders(resp);
            final OutputStream os;
            resp.setCharacterEncoding("utf-8");
            os = resp.getOutputStream();
            transferStreams(url.openStream(), os);
        }
