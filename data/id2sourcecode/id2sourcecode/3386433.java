    public void _submitResource(String ID, Object obj) {
        synchronized (resources) {
            int properIndex = -1;
            if (ID.length() == 0) properIndex = 0; else if (resources.size() > 0) {
                int start = 0;
                int end = resources.size() - 1;
                int mid = 0;
                while (start <= end) {
                    mid = (end + start) / 2;
                    int comp = ((String) resources.elementAt(mid, 1)).compareToIgnoreCase(ID);
                    if (comp == 0) return; else if (comp > 0) end = mid - 1; else start = mid + 1;
                }
                if (end < 0) properIndex = 0; else if (start >= resources.size()) properIndex = resources.size() - 1; else properIndex = mid;
            }
            Object prepared = prepareObject(obj);
            Boolean preparedB = new Boolean(prepared != obj);
            if (properIndex < 0) resources.addElement(ID, prepared, preparedB); else {
                int comp = ((String) resources.elementAt(properIndex, 1)).compareToIgnoreCase(ID);
                if (comp > 0) resources.insertElementAt(properIndex, ID, prepared, preparedB); else if (properIndex == resources.size() - 1) resources.addElement(ID, prepared, preparedB); else resources.insertElementAt(properIndex + 1, ID, prepared, preparedB);
            }
        }
    }
