    protected int findIndexOfViewContainer(BlockViewContainerPart vcp) {
        if (vcp == null) {
            return -1;
        }
        int min = 0;
        int max = m_vcps.size();
        int y = vcp.getBounds().y;
        while (min < max) {
            int mid = (min + max) / 2;
            if (mid == min) {
                break;
            }
            BlockViewContainerPart vcp2 = (BlockViewContainerPart) m_vcps.get(mid);
            int y2 = vcp2.getBounds().y;
            if (y2 < y) {
                min = mid;
            } else {
                max = mid;
            }
        }
        for (int i = min; i < m_vcps.size(); i++) {
            BlockViewContainerPart vcp2 = (BlockViewContainerPart) m_vcps.get(i);
            int y2 = vcp2.getBounds().y;
            if (y2 > y) {
                return -1;
            } else if (vcp2 == vcp) {
                return i;
            }
        }
        return -1;
    }
