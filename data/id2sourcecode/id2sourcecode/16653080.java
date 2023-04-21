    public void changeEvent(ScrEvent event) {
        int index;
        index = indexOf(event);
        if (index == NO_SUCH_EVENT || index == EMPTY_COLLECTION) return;
        args.clear();
        args.addInt(index);
        args.addInt(event.getTime());
        args.addInt(event.getPitch());
        args.addInt(event.getVelocity());
        args.addInt(event.getDuration());
        args.addInt(event.getChannel());
        try {
            send(FtsSymbol.get("change_event"), args);
        } catch (IOException e) {
            System.err.println("FtsObjectWithEditor: I/O Error sending change_event Message!");
            e.printStackTrace();
        }
        notifyObjectChanged(event);
    }
