    protected int findElementIndexOfViewContainer(BlockViewContainerPart vcp) {
        int min = 0;
        int max = m_elements.size();
        int y = vcp.getBounds().y;
        while (min < max) {
            int mid = (min + max) / 2;
            if (mid == min) {
                break;
            }
            Element element = (Element) m_elements.get(mid);
            int y2 = element.m_vcp.getBounds().y;
            if (y2 <= y) {
                min = mid;
            } else {
                max = mid;
            }
        }
        if (min >= 0 && min < m_elements.size()) {
            Element element = (Element) m_elements.get(min);
            if (element.m_vcp == vcp) {
                return min;
            }
        }
        return -1;
    }
