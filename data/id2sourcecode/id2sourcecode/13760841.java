    private void writeComment(Writer writer, OldEventInfo event) {
        try {
            int iSize2 = event.m_Comments.size();
            writer.write(m_szCommHead);
            for (int i2 = 0; i2 < iSize2; i2++) {
                LJComment comment = (LJComment) event.m_Comments.get(i2);
                if (comment.m_iState != comment.DELETED) {
                    writer.write(m_szThreadHead);
                    String szComment = insertThread(comment, 0);
                    writer.write(szComment);
                    writer.write(m_szThreadFoot);
                }
            }
            writer.write(m_szCommFoot);
        } catch (java.io.IOException e) {
            System.err.println(e);
        }
    }
