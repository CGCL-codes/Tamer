    public static void rateBlogEntry(String UUID, int rank, String username, Session session) throws RepositoryException {
        Node blogEntryNode = session.getNodeByUUID(UUID);
        long currentRank = blogEntryNode.getProperty("blog:rate").getLong();
        long newRank = (currentRank + rank) / 2;
        blogEntryNode.setProperty("blog:rate", newRank);
        session.save();
    }
