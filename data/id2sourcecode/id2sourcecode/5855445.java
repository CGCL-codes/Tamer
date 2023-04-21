    public ModelAndView searchChannelUrl(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ParseException {
        ModelAndView view = new ModelAndView("articles");
        if (request.getParameter("searchObject") != null) {
            String typeSearch = request.getParameter("searchType");
            if (typeSearch.equals("url")) {
                view.addObject("articles", channelController.getChannelsLikeUrl(request.getParameter("searchObject")));
            } else if (typeSearch.equals("title")) {
                view.addObject("articles", channelController.getChannelsLikeTitle(request.getParameter("searchObject")));
            } else if (typeSearch.equals("item")) {
                return new ModelAndView("forward:rssReader/overview/searchResults");
            }
            return view;
        }
        return getCurrent(request);
    }
