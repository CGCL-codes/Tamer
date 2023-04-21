    private void saveFavorite(HttpServletRequest request, HttpServletResponse response, EHCallObject ehc) {
        try {
            URL url = new URL(request.getParameter("url"));
            StringWriter sw = new StringWriter();
            InputStreamReader isr = new InputStreamReader(url.openStream());
            char[] c = new char[100];
            int len = 0;
            while ((len = isr.read(c, 0, 100)) != -1) sw.write(c, 0, len);
            String newFav = (String) (sw.toString());
            newFav = newFav.replace('\n', ' ');
            Utils.debug("HTTPFrontEnd", "Favorite TBA: " + newFav);
            Cookie[] cookies = request.getCookies();
            for (int i = 0; i < cookies.length; i++) {
                String name = cookies[i].getName();
                if (name.equals("FavoriteGenerators")) {
                    String favorites = cookies[i].getValue();
                    Utils.debug("HTTPFrontEnd", "Favorites cookie val: " + favorites);
                    int from = newFav.indexOf("<generator");
                    int at = favorites.indexOf("</generators>");
                    favorites = favorites.substring(0, at - 1) + newFav.substring(from) + favorites.substring(at);
                    Utils.debug("HTTPFrontEnd", "Updated favorites: " + favorites);
                    cookies[i].setValue(favorites);
                    response.addCookie(cookies[i]);
                    return;
                }
            }
            int from = newFav.indexOf("<generator");
            String initVal = "<?xml version=\"1.0\"?>" + "<generators>" + newFav.substring(from) + "</generators>";
            Utils.debug("HTTPFrontEnd", "Initial favorite cookie val: " + initVal);
            Cookie cookie = new Cookie("FavoriteGenerators", initVal);
            response.addCookie(cookie);
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<HTML>");
            out.println("<HEAD><TITLE> Favorite added </TITLE></HEAD>");
            out.println("<BODY>");
            out.println("<p>Successfully added to favorites!</p>");
            out.println("</BODY></HTML>");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
