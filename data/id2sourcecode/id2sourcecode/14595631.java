        public int update(Item item) throws InvalidDataAccessApiUsageException {
            return super.update(new Object[] { item.getTitle(), item.getUrl(), item.getDescription(), item.getComments(), new Long(item.getPostedDate()), Boolean.valueOf(item.isArticleRead()), item.getChannelID(), item.getLastModified(), item.getLastETag(), Boolean.valueOf(item.isRemove()), new Integer(item.getPreferance()), Boolean.valueOf(item.isFetched()) });
        }
