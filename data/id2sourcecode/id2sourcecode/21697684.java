    private boolean doHandleDocument(SPDocument spdoc, String stylesheet, TreeMap<String, Object> paramhash, PfixServletRequest preq, HttpServletResponse res, HttpSession session, ByteArrayOutputStream output) throws IOException, PustefixCoreException {
        boolean modified_or_no_etag = true;
        String etag_incoming = preq.getRequest().getHeader("If-None-Match");
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException x) {
            throw new RuntimeException("Can't create message digest", x);
        }
        DigestOutputStream digestOutput = new DigestOutputStream(output, digest);
        try {
            hookBeforeRender(preq, spdoc, paramhash, stylesheet);
            render(spdoc, getRendering(preq), res, paramhash, stylesheet, digestOutput, preq);
        } finally {
            hookAfterRender(preq, spdoc, paramhash, stylesheet);
        }
        byte[] digestBytes = digest.digest();
        String etag_outgoing = MD5Utils.byteToHex(digestBytes);
        res.setHeader("ETag", etag_outgoing);
        if (getRendering(preq) == RENDERMODE.RENDER_NORMAL && etag_incoming != null && etag_incoming.equals(etag_outgoing)) {
            res.setContentType(null);
            res.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
            LOGGER.info("*** Reusing UI: " + spdoc.getPagename());
            modified_or_no_etag = false;
        } else {
            output.writeTo(res.getOutputStream());
        }
        return modified_or_no_etag;
    }
