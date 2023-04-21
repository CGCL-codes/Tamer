    public void contentsChanged(ActiveListEvent event) {
        threadLock.writeLock().lock();
        info.setChanges(info.getChanges() + event.getY() - event.getX() + 1);
        info.setEvents(info.getEvents() + 1);
        threadLock.writeLock().unlock();
    }
