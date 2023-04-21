    public void onSharedObject(RTMPConnection conn, Channel channel, Header source, SharedObjectMessage object) {
        final ISharedObject so;
        final String name = object.getName();
        IScope scope = conn.getScope();
        if (scope == null) {
            SharedObjectMessage msg = new SharedObjectMessage(name, 0, object.isPersistent());
            msg.addEvent(new SharedObjectEvent(ISharedObjectEvent.Type.CLIENT_STATUS, "SharedObject.NoObjectFound", "error"));
            conn.getChannel((byte) 3).write(msg);
            return;
        }
        ISharedObjectService sharedObjectService = (ISharedObjectService) getScopeService(scope, ISharedObjectService.SHARED_OBJECT_SERVICE, SharedObjectService.class);
        if (!sharedObjectService.hasSharedObject(scope, name)) {
            if (!sharedObjectService.createSharedObject(scope, name, object.isPersistent())) {
                SharedObjectMessage msg = new SharedObjectMessage(name, 0, object.isPersistent());
                msg.addEvent(new SharedObjectEvent(ISharedObjectEvent.Type.CLIENT_STATUS, "SharedObject.ObjectCreationFailed", "error"));
                conn.getChannel((byte) 3).write(msg);
                return;
            }
        }
        so = sharedObjectService.getSharedObject(scope, name);
        so.dispatchEvent(object);
    }
