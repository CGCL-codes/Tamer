        public String[] getCategories() {
            String[] categories = super.getCategories();
            String channel = getChannel();
            if (!"".equals(channel)) for (int i = 0; i < categories.length; i++) categories[i] = categories[i] + "." + channel;
            return categories;
        }
