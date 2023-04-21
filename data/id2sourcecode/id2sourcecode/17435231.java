    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        load(req, resp);
        try {
            _title = "Issue CSR";
            login(req);
            if (!isUserSet()) return;
            String resourceName = _user.getResourceName();
            String csr = req.getParameter("csr");
            if (csr == null) {
                InputStream bodyStream = req.getInputStream();
                if (bodyStream == null) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }
                ByteArrayOutputStream bodyByteStream = new ByteArrayOutputStream();
                ;
                int current = -1;
                while ((current = bodyStream.read()) != -1) bodyByteStream.write(current);
                csr = new String(bodyByteStream.toByteArray());
            }
            CertificationAuthority ca = CertificationAuthority.getInstance();
            Certificate cert = ca.processCsr(csr, _user);
            resp.setStatus(HttpServletResponse.SC_OK);
            ServletOutputStream respBody = resp.getOutputStream();
            String format = req.getParameter("responseFormat");
            if (format != null) {
                if (format.equals("PEM")) {
                    resp.setContentType("text/plain");
                    respBody.write(CertificateUtil.certificateToPEM(cert));
                    respBody.flush();
                    return;
                }
            }
            respBody.write(cert.getEncoded());
            respBody.flush();
        } catch (CertificationAuthorityException e) {
            log.error("Could not process POST request", e);
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        } catch (CertificateEncodingException e) {
            log.error("Could not process POST request", e);
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
