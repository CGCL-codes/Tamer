    protected Rectangle calcDropRect(int x, int y) {
        if (m_supportsListInsertion) {
            int min = 0;
            int max = m_elements.size();
            while (min < max) {
                int mid = (min + max) / 2;
                if (mid == min) {
                    break;
                }
                Element element = (Element) m_elements.get(mid);
                Rectangle r = element.m_vcp.getBounds();
                if (y < r.y) {
                    max = mid;
                } else {
                    min = mid;
                }
            }
            if (min < max) {
                Element element = (Element) m_elements.get(min);
                Rectangle r = element.m_vcp.getBounds();
                if (y < r.y + r.height / 2) {
                    m_lastInsertionPoint = min;
                    return new Rectangle(r.x + 2, r.y - 2, r.width - 5, 4);
                } else {
                    m_lastInsertionPoint = min + 1;
                    return new Rectangle(r.x + 2, r.y + r.height - 2, r.width - 5, 4);
                }
            }
        }
        m_lastInsertionPoint = -1;
        return null;
    }
