    protected void subscribe(String channelId, String methodName) {
        Method method = null;
        Class<?> c = this.getClass();
        while (c != null && c != Object.class) {
            Method[] methods = c.getDeclaredMethods();
            for (int i = methods.length; i-- > 0; ) {
                if (methodName.equals(methods[i].getName())) {
                    if (method != null) throw new IllegalArgumentException("Multiple methods called '" + methodName + "'");
                    method = methods[i];
                }
            }
            c = c.getSuperclass();
        }
        if (method == null) throw new NoSuchMethodError(methodName);
        int params = method.getParameterTypes().length;
        if (params < 2 || params > 4) throw new IllegalArgumentException("Method '" + methodName + "' does not have 2or3 parameters");
        if (!Client.class.isAssignableFrom(method.getParameterTypes()[0])) throw new IllegalArgumentException("Method '" + methodName + "' does not have Client as first parameter");
        Channel channel = _bayeux.getChannel(channelId, true);
        if (((ChannelImpl) channel).getChannelId().isWild()) {
            final Method m = method;
            Client wild_client = _bayeux.newClient(_name + "-wild");
            wild_client.addListener(_listener instanceof MessageListener.Asynchronous ? new AsyncWildListen(wild_client, m) : new SyncWildListen(wild_client, m));
            channel.subscribe(wild_client);
        } else {
            _methods.put(channelId, method);
            channel.subscribe(_client);
        }
    }
