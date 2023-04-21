    protected IVisualPart hittest(int x, int y, boolean favorParent) {
        int margin = favorParent ? 3 : 0;
        try {
            Element element = (Element) m_elements.get(m_previousHittestElementIndex);
            if (element != null) {
                Rectangle r = element.m_vcp.getBounds();
                if (y >= r.y + margin && y < r.y + r.height - margin) {
                    return element.m_vcp;
                }
            }
        } catch (Exception ex) {
        }
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
            if (y >= r.y + margin && y < r.y + r.height - margin) {
                m_previousHittestElementIndex = min;
                return element.m_vcp;
            }
        }
        m_previousHittestElementIndex = -1;
        return null;
    }
