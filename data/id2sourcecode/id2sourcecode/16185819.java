    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String[] url = DashboardUtils.urlToParams(request.getRequestURI());
        String projectName = url[url.length - 2];
        String category = url[url.length - 1];
        File svgFile = getPanopticodeOutput(projectName, category);
        if (!svgFile.exists()) {
            return pictureNotExist(projectName, response);
        }
        response.setContentType("image/svg+xml");
        response.getWriter().write(FileUtils.readFileToString(svgFile, "UTF-8"));
        return null;
    }
