    public synchronized void add(Object object) {
        if (_queue.size() == 0) {
            _queue.addElement(object);
        } else {
            int start = 0;
            int end = _queue.size() - 1;
            if (_comparator.compare(object, _queue.firstElement()) < 0) {
                _queue.insertElementAt(object, 0);
            } else if (_comparator.compare(object, _queue.lastElement()) > 0) {
                _queue.addElement(object);
            } else {
                while (true) {
                    int midpoint = start + (end - start) / 2;
                    if (((end - start) % 2) != 0) {
                        midpoint++;
                    }
                    int result = _comparator.compare(object, _queue.elementAt(midpoint));
                    if (result == 0) {
                        _queue.insertElementAt(object, midpoint);
                        break;
                    } else if ((start + 1) == end) {
                        _queue.insertElementAt(object, end);
                        break;
                    } else {
                        if (result > 0) {
                            start = midpoint;
                        } else {
                            end = midpoint;
                        }
                    }
                }
            }
        }
    }
