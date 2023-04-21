    public boolean errorResponse(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (errorRate == 0) return false;
        if (errorOnUrl) {
            String url = request.getRequestURL() + request.getQueryString();
            byte[] digest = md5.digest(url.getBytes());
            int hash = Math.abs(digest[4] % 100);
            log.debug(url.toString() + " -> " + hash + ">" + errorRate + "?");
            if (hash > errorRate) return false;
        } else {
            int rnd = RND.nextInt(100);
            if (rnd > errorRate) return false;
        }
        response.sendError(404);
        log.debug("Error response sent");
        return true;
    }
