            thread.start();
        } else {
            thread.readObject();
        }
        return thread;
    }

    public LoaderThread writeObject(Object obj, String filename) {
        return writeObject(obj, filename, null);
    }

    public LoaderThread writeObject(Object obj, String filename, Observer observer) {
        LoaderThread thread = new LoaderThread(this, filename, obj);
        if (observer != null) {
            thread.addObserver(observer);
            thread.start();
        } else {
            thread.writeObject();
        }
        return thread;
    }
