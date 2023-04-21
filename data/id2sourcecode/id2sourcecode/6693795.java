    public void writeMessage(String threadName, String message) {
        if (messages.get(threadName) == null) {
            List msgs = new ArrayList(20);
            msgs.add(message);
            messages.put(threadName, msgs);
        } else {
            List msgs = (List) messages.get(threadName);
            if (msgs.size() >= 20) msgs.clear();
            msgs.add(message);
        }
        setChanged();
        notifyObservers(new String[] { Messages.ConsoleView_Thread + threadName, message });
    }
