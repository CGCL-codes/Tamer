    @HandlerMethod(id = "handleKBaseObsolete")
    public synchronized void handlekBaseObsolete(int obsoleteIndex, LinkedList<SchedulingItem> priorityQueue) {
        if (getStatus(obsoleteIndex)) {
            for (int i = findElementIndex(obsoleteIndex); i < position; i++) {
                if (elements.length > i + 1) {
                    elements[i] = elements[i + 1];
                }
            }
            position--;
        } else {
        }
        for (int i = 0; i < priorityQueue.size(); i++) {
            int value = priorityQueue.get(i).getIndex();
            BufferElement elt = findElement(value);
            if (elt == null) {
                put(value);
                break;
            }
        }
    }
