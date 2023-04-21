        public void removeFilter(Filter filter) {
            int n = 0;
            for (; n < filters.length && filters[n] != filter; n++) ;
            if (n != filters.length) {
                filtersPanel.remove(filters[n]);
                for (; n < filters.length - 1; n++) filters[n] = filters[n + 1];
            }
        }
