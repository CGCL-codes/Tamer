    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String categoryId = request.getParameter("category");
        Category category = null;
        if (StringUtils.isNotEmpty(categoryId)) {
            category = feedDao.getCategory(categoryId);
        }
        if (category == null) {
            category = repository.getDefaultCategory();
        }
        SortedMap<Feed, ChannelIF> channelMap = repository.getChannelMap(category);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("channelMap", channelMap);
        map.put("categoryId", category.getId());
        map.put("categories", repository.getCategories());
        map.put("mobile", CommonUtils.isMobile(request));
        return new ModelAndView("javarss", map);
    }
