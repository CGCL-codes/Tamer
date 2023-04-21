    public static void transmit(String service, Configuration config, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String param_lang = ConnectorServlet.getParameter(request, "language");
        if (param_lang == null) param_lang = "";
        param_lang = config.getWMS().getLangOrDefaultLang(param_lang);
        String surl;
        if (service.equalsIgnoreCase("WMS")) {
            surl = config.getWMS().getServerByLang(param_lang).getUrl();
        } else {
            surl = config.getWFS().getServerByLang(param_lang).getUrl();
        }
        if (surl.contains("?")) surl += "&"; else surl += "?";
        Enumeration param_enum = request.getParameterNames();
        while (param_enum.hasMoreElements()) {
            String param_name = param_enum.nextElement().toString();
            String param_value = request.getParameter(param_name);
            if (param_name.equalsIgnoreCase("LAYERS") || param_name.equalsIgnoreCase("LAYER") || param_name.equalsIgnoreCase("TYPENAME")) {
                surl += param_name + "=";
                String[] layerNames = StringUtils.split(param_value, ",");
                for (int i = 0; i < layerNames.length; i++) {
                    String newLayerName = layerNames[i];
                    for (LayerName ln : config.getWMS().getLayerList()) {
                        if (layerNames[i].equalsIgnoreCase(ln.getName())) {
                            newLayerName = ln.getOriginalNameByLang(param_lang);
                        }
                    }
                    surl += newLayerName + ",";
                }
                surl = StringUtils.substringBeforeLast(surl, ",") + "&";
            } else {
                surl += param_name + "=" + param_value + "&";
            }
        }
        String strRequest = surl;
        URL url2Request = new URL(strRequest);
        URLConnection conn = url2Request.openConnection();
        response.setContentType(conn.getContentType());
        if (conn.getContentType().contains("image")) {
            ImageIO.write(ImageIO.read(url2Request), StringUtils.substringAfter(conn.getContentType(), "image/"), response.getOutputStream());
        } else {
            DataInputStream buffin = new DataInputStream(new BufferedInputStream(conn.getInputStream()));
            String strLine = null;
            while ((strLine = buffin.readLine()) != null) {
                response.getWriter().println(strLine);
            }
            buffin.close();
        }
    }
