    public RemoteIncrementalFaultList(ObjectContext context, Query paginatedQuery) {
        this.metadata = paginatedQuery.getMetaData(context.getEntityResolver());
        if (metadata.getPageSize() <= 0) {
            throw new IllegalArgumentException("Page size must be positive: " + metadata.getPageSize());
        }
        this.pageSize = metadata.getPageSize();
        this.helper = (metadata.isFetchingDataRows()) ? (ListHelper) new DataRowListHelper() : new PersistentListHelper();
        this.context = context;
        this.cacheKey = generateCacheKey();
        IncrementalQuery query = new IncrementalQuery(paginatedQuery, cacheKey);
        QueryResponse response = context.getChannel().onQuery(context, query);
        List firstPage = response.firstList();
        if (firstPage.size() > pageSize) {
            throw new IllegalArgumentException("Returned page size (" + firstPage.size() + ") exceeds requested page size (" + pageSize + ")");
        } else if (firstPage.size() < pageSize) {
            this.elements = new ArrayList(firstPage);
            unfetchedObjects = 0;
        } else {
            if (response instanceof IncrementalListResponse) {
                int fullListSize = ((IncrementalListResponse) response).getFullSize();
                this.unfetchedObjects = fullListSize - firstPage.size();
                this.elements = new ArrayList(fullListSize);
                elements.addAll(firstPage);
                for (int i = pageSize; i < fullListSize; i++) {
                    elements.add(PLACEHOLDER);
                }
            } else {
                this.elements = new ArrayList(firstPage);
                unfetchedObjects = 0;
            }
        }
    }
