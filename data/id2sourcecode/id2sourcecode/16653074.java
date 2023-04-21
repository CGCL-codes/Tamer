    public void addEvent(ScrEvent event) {
        int index;
        event.setDataModel(this);
        index = getIndexAfter(event.getTime());
        if (index == EMPTY_COLLECTION) index = 0; else if (index == NO_SUCH_EVENT) index = events_fill_p;
        makeRoomAt(index);
        events[index] = event;
        args.clear();
        args.addInt(index);
        args.addInt(event.getTime());
        args.addInt(event.getPitch());
        args.addInt(event.getVelocity());
        args.addInt(event.getDuration());
        args.addInt(event.getChannel());
        try {
            send(FtsSymbol.get("add_event"), args);
        } catch (IOException e) {
            System.err.println("FtsObjectWithEditor: I/O Error sending add_event Message!");
            e.printStackTrace();
        }
        notifyObjectAdded(event, index);
        if (isInGroup()) postEdit(new UndoableAdd(event));
    }
