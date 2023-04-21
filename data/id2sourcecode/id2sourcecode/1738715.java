    public void saveOrUpdate(AnyItemType anyItem) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            m_bindingProvider.marshaller(anyItem, outputStream);
        } catch (AnyItemBindingException e) {
            e.printStackTrace();
        }
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Document document = XMLHelper.fileinputStreamToDocument(inputStream);
        StoreAnyItem storeItem = PersistNewsMLFacade.instance.buildStoreAnyItem(anyItem);
        NewsMLDocument newsMLDocument = new NewsMLDocument();
        newsMLDocument.setVersion(anyItem.getItemVersion());
        newsMLDocument.setXml(document);
        newsMLDocument.setStoreAnyItem(storeItem);
        try {
            boolean newindex = m_storeItemDao.saveOrUpdate(storeItem, newsMLDocument);
            if (newindex) {
                m_indexSupport.onSave(anyItem);
                System.out.println("new index");
            } else {
                m_indexSupport.onUpdate(anyItem);
                System.out.println("update index");
            }
        } catch (DataAccessException e) {
            System.out.println("roll back cause : " + e.getMessage());
            return;
        } catch (IndexException e) {
            System.out.println("roll back cause Indexing: " + e.getMessage());
            return;
        }
    }
