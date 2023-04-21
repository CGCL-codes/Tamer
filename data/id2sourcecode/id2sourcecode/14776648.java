        private int getStartIndex(int year, int start, int end) {
            if (end == start) return start;
            int pivot = (start + end) / 2;
            int y = ((Event) events.get(pivot)).getTime().getYear();
            if (y < year) return getStartIndex(year, pivot + 1, end);
            return getStartIndex(year, start, pivot);
        }
