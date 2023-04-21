    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (this.md == null) {
            try {
                this.md = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                throw new ServletException(e);
            }
        }
        PrintWriter pw = resp.getWriter();
        String ip = req.getRemoteAddr();
        Timestamp time = new Timestamp(System.currentTimeMillis());
        String token = new BigInteger(this.md.digest((ip + ":" + time.getTime()).getBytes())).abs().toString(16);
        resp.setContentLength(token.length());
        pw.print(token);
        pw.close();
        try {
            PreparedStatement statement = createStatement();
            statement.setString(1, ip);
            statement.setTimestamp(2, time);
            statement.setString(3, token);
            statement.setString(4, req.getHeader("User-Agent"));
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
