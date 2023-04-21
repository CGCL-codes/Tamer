    protected int[] findStartEndElementIndices(int startY, int endY) {
        int[] result = new int[2];
        int lo = 0;
        int hi = m_elements.size() - 1;
        while (lo < hi - 1) {
            int mid = (lo + hi) / 2;
            Element element = (Element) m_elements.get(mid);
            if (element.m_vcp.getBounds().y < startY) {
                lo = mid;
            } else {
                hi = mid;
            }
        }
        result[0] = lo;
        hi = m_elements.size() - 1;
        while (lo < hi - 1) {
            int mid = (lo + hi) / 2;
            Element element = (Element) m_elements.get(mid);
            if (element.m_vcp.getBounds().y > endY) {
                hi = mid;
            } else {
                lo = mid;
            }
        }
        result[1] = hi;
        return result;
    }
