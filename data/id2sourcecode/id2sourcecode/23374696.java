    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        InstanciaDelegate delegate = RegistroManager.recuperarInstancia(request);
        if (delegate == null) {
            ActionErrors errors = new ActionErrors();
            errors.add(null, new ActionError("errors.session"));
            saveErrors(request, errors);
            return mapping.findForward("fail");
        }
        AyudaPantalla ayudaPantalla = delegate.obtenerAyudaPantalla();
        TraAyudaPantalla traAyudaPantalla = (TraAyudaPantalla) ayudaPantalla.getTraduccion();
        String urlVideo = traAyudaPantalla.getUrlVideo();
        if (urlVideo == null) {
            return null;
        }
        URL url = new URL(urlVideo);
        URLConnection urlConnection = url.openConnection();
        urlConnection.connect();
        String tipoMime = urlConnection.getContentType();
        log.debug("Tipo Mime: " + tipoMime);
        Map videoOptions = new HashMap();
        if (tipoMime.indexOf("mplay") != -1 || tipoMime.indexOf("avi") != -1 || tipoMime.indexOf("asf") != -1 || tipoMime.indexOf("mpeg") != -1 || tipoMime.indexOf("wmv") != -1 || tipoMime.indexOf("ms") != -1) {
            videoOptions.put("id", "MediaPlayer");
            videoOptions.put("codeBase", "http://activex.microsoft.com/activex/controls/mplayer/en/nsmp2inf.cab#Version=6,4,7,1112");
            videoOptions.put("classId", "CLSID:22D6F312-B0F6-11D0-94AB-0080C74C7E95");
            videoOptions.put("mimeType", "application/x-mplayer2");
            videoOptions.put("urlVideo", urlVideo);
            videoOptions.put("pluginSpage", "http://www.microsoft.com/Windows/Downloads/Contents/Products/MediaPlayer/");
        }
        if (tipoMime.indexOf("quicktime") != -1) {
            videoOptions.put("id", "");
            videoOptions.put("codeBase", "http://www.apple.com/qtactivex/qtplugin.cab#version=6,0,2,0");
            videoOptions.put("classId", "clsid:02BF25D5-8C17-4B23-BC80-D3488ABDDC6B");
            videoOptions.put("mimeType", "video/quicktime");
            videoOptions.put("urlVideo", urlVideo);
            videoOptions.put("pluginSpage", "http://www.apple.com/quicktime/download/indext.html");
        }
        if (tipoMime.indexOf("real") != -1) {
            videoOptions.put("id", "RVOCX");
            videoOptions.put("codeBase", "");
            videoOptions.put("classId", "clsid:CFCDAA03-8BE4-11cf-B84B-0020AFBBCCFA");
            videoOptions.put("mimeType", "audio/x-pn-realaudio-plugin");
            videoOptions.put("urlVideo", urlVideo);
            videoOptions.put("pluginSpage", "");
        }
        request.setAttribute("videoOptions", videoOptions);
        log.debug("entra success");
        return mapping.findForward("success");
    }
