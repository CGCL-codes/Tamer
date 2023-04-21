    protected URLConnection toAcceptLanguageConnection(URL url, HttpServletRequest request) throws Exception {
        URLConnection tempConnection = url.openConnection();
        tempConnection.setRequestProperty("Accept-Language", request.getHeader("Accept-Language"));
        return tempConnection;
    }
