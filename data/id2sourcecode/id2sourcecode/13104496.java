    public void testFilterValues() throws Exception {
        Filter<Address> filter = Filter.filterFor(Address.class, "addressID = ?").bind();
        FilterValues<Address> fv = filter.initialFilterValues().with(5);
        FilterValues<Address> read = writeAndRead(fv);
        assertTrue(fv.getFilter() == read.getFilter());
        assertEquals(fv, read);
        filter = Filter.filterFor(Address.class, "addressID = ? | (addressZip > ? & customData < ?)").bind();
        fv = filter.initialFilterValues().withValues(5, "12345", "foo");
        read = writeAndRead(fv);
        assertTrue(fv.getFilter() == read.getFilter());
        assertEquals(fv, read);
        Filter<Address> inner = Filter.getOpenFilter(Address.class);
        inner = inner.and("addressZip", RelOp.GT, "12345");
        inner = inner.and("customData < ?");
        filter = Filter.filterFor(Address.class, "addressID = ?").or(inner);
        filter = filter.bind();
        fv = filter.initialFilterValues().withValues(5, "foo");
        read = writeAndRead(fv);
        assertTrue(fv.getFilter() == read.getFilter());
        assertEquals(fv, read);
    }
