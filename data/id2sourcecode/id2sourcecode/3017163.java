    public static boolean errorResponse(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (ERROR_RATE == 0) return false;
        if (ERROR_ON_URL) {
            String url = request.getRequestURL() + request.getQueryString();
            byte[] digest = MD5.digest(url.getBytes());
            int hash = Math.abs(digest[4] % 100);
            System.out.println(url.toString() + " -> " + hash + ">" + ERROR_RATE + "?");
            if (hash > ERROR_RATE) return false;
        } else {
            int rnd = RAND.nextInt(100);
            if (rnd > ERROR_RATE) return false;
        }
        response.sendError(404);
        System.out.println("Error sent");
        return true;
    }
