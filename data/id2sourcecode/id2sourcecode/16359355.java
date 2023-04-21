    public static String getKey(ItemIF item) {
        Date date = item.getDate();
        URL location = item.getChannel().getLocation();
        String title = item.getTitle();
        return (location == null ? "" : location.toString()) + "-" + (date == null ? 0 : date.getTime()) + "-" + (title == null ? 0 : title.hashCode());
    }
