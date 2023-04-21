    private void decreaseArraySize(int arrayPosition) {
        ListeningBean[] tempBeans = new ListeningBean[listeners.length - 1];
        for (int i = 0; i < arrayPosition; i++) {
            tempBeans[i] = listeners[i];
        }
        for (int i = arrayPosition; i < tempBeans.length; i++) {
            tempBeans[i] = listeners[i + 1];
        }
        this.listeners = tempBeans;
    }
