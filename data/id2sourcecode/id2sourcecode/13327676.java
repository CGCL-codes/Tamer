    public void insertTestData() throws Exception {
        newsBlock = null;
        newsBlock = new NewsBlockType();
        newsBlock.setIdSiteLanguage(TestCaseSiteAbstract.testSite.idRuSiteLanguage);
        for (int i = 0; i < COUNT_TEST_NEWS_GROUP; i++) {
            NewsGroupType newsGroup = new NewsGroupType();
            newsBlock.addNewsGroup(newsGroup);
            PreparedStatement ps = null;
            int i1 = 0;
            try {
                CustomSequenceType seqSite = new CustomSequenceType();
                seqSite.setSequenceName("SEQ_WM_NEWS_LIST");
                seqSite.setTableName("WM_NEWS_LIST");
                seqSite.setColumnName("ID_NEWS");
                Long seqValue = new Long(testAbstract.db_.getSequenceNextValue(seqSite));
                newsGroup.setNewsGroupId(seqValue);
                newsGroup.setNewsGroupName(NEWS_GROUP_TEXT + i);
                newsGroup.setMaxNews(new Long(4));
                newsGroup.setOrderValue(new Integer(20 - i));
                newsGroup.setNewsGroupCode(i == 0 ? NEWS_GROUP_CODE : "");
                assertFalse("Error get new value of sequence for table " + seqSite.getTableName(), seqValue == null);
                ps = testAbstract.db_.prepareStatement("insert into WM_NEWS_LIST " + "(ID_NEWS, NAME_NEWS, COUNT_NEWS, ORDER_FIELD, CODE_NEWS_GROUP, ID_SITE_SUPPORT_LANGUAGE) " + "values" + "(?, ?, ?, ?, ?, ?)");
                RsetTools.setLong(ps, 1, newsGroup.getNewsGroupId());
                ps.setString(2, newsGroup.getNewsGroupName());
                ps.setObject(3, newsGroup.getMaxNews());
                ps.setObject(4, newsGroup.getOrderValue());
                ps.setString(5, newsGroup.getNewsGroupCode());
                RsetTools.setLong(ps, 6, TestCaseSiteAbstract.testSite.idRuSiteLanguage);
                i1 = ps.executeUpdate();
                assertFalse("Error insert news block", i1 == 0);
                testAbstract.db_.commit();
                DatabaseManager.close(ps);
                ps = null;
                for (int j = 0; j < COUNT_TEST_NEWS_ITEM; j++) {
                    NewsItemType newsItem = new NewsItemType();
                    newsGroup.addNewsItem(newsItem);
                    CustomSequenceType seqNewsItem = new CustomSequenceType();
                    seqNewsItem.setSequenceName("SEQ_WM_NEWS_ITEM");
                    seqNewsItem.setTableName("WM_NEWS_ITEM");
                    seqNewsItem.setColumnName("ID");
                    Long seqNewsItemValue = new Long(testAbstract.db_.getSequenceNextValue(seqNewsItem));
                    newsItem.setNewsItemId(seqNewsItemValue);
                    Timestamp stamp = DateTools.getCurrentTime();
                    stamp.setNanos(0);
                    newsItem.setNewsDateTime(stamp);
                    newsItem.setNewsDate(DateUtils.getStringDate(newsItem.getNewsDateTime(), "dd.MMM.yyyy", StringTools.getLocale(TestSite.TEST_LANGUAGE)));
                    newsItem.setNewsTime(DateUtils.getStringDate(newsItem.getNewsDateTime(), "HH:mm", StringTools.getLocale(TestSite.TEST_LANGUAGE)));
                    newsItem.setNewsHeader(NEWS_ITEM_TEXT_HEADER + " " + j);
                    newsItem.setNewsAnons(NEWS_ITEM_TEXT_ANONS + " " + j);
                    newsItem.setNewsText(NEWS_ITEM_TEXT_TEXT + " " + j);
                    newsItem.setUrlToFullNewsItem(NEWS_ITEM_URL);
                    newsItem.setToFullItem(NEWS_ITEM_TO_URL);
                    assertFalse("Error get new value of sequence for table " + seqNewsItem.getTableName(), seqNewsItemValue == null);
                    ps = testAbstract.db_.prepareStatement("insert into WM_NEWS_ITEM " + "(ID, ID_NEWS, EDATE, HEADER, ANONS) " + "values" + "(?, ?, " + testAbstract.db_.getNameDateBind() + ", ?, ?)");
                    RsetTools.setLong(ps, 1, newsItem.getNewsItemId());
                    RsetTools.setLong(ps, 2, newsGroup.getNewsGroupId());
                    testAbstract.db_.bindDate(ps, 3, (Timestamp) newsItem.getNewsDateTime());
                    ps.setString(4, newsItem.getNewsHeader());
                    ps.setString(5, newsItem.getNewsAnons());
                    i1 = ps.executeUpdate();
                    assertFalse("Error insert news item", i1 == 0);
                    long mills = System.currentTimeMillis();
                    while (true) if ((System.currentTimeMillis() - mills) > 1000) break;
                    testAbstract.db_.commit();
                    DatabaseManager.close(ps);
                    ps = null;
                    CustomSequenceType seqNewsItemText = new CustomSequenceType();
                    seqNewsItemText.setSequenceName("SEQ_WM_NEWS_ITEM_TEXT");
                    seqNewsItemText.setTableName("WM_NEWS_ITEM_TEXT");
                    seqNewsItemText.setColumnName("ID_MAIN_NEWS_TEXT");
                    Long idNewsItemText = new Long(testAbstract.db_.getSequenceNextValue(seqNewsItemText));
                    assertFalse("Error get new value of sequence for table " + seqNewsItemText.getTableName(), idNewsItemText == null);
                    ps = testAbstract.db_.prepareStatement("insert into WM_NEWS_ITEM_TEXT " + "(ID_MAIN_NEWS_TEXT, ID, TEXT) " + "values" + "(?, ?, ?)");
                    RsetTools.setLong(ps, 1, idNewsItemText);
                    RsetTools.setLong(ps, 2, newsItem.getNewsItemId());
                    ps.setString(3, newsItem.getNewsText());
                    i1 = ps.executeUpdate();
                    assertFalse("Error insert news item text", i1 == 0);
                    testAbstract.db_.commit();
                    DatabaseManager.close(ps);
                    ps = null;
                }
            } catch (Exception e) {
                testAbstract.db_.rollback();
                throw e;
            } finally {
                DatabaseManager.close(ps);
                ps = null;
            }
        }
        newsBlock.validate();
    }
