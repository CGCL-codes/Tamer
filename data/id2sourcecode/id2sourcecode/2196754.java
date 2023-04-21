    public void handleSystemOut(String[] request, double timestamp) {
        String command = request[0];
        if (command.equals("route")) {
            dispatcher.dispatch(new Message(request[2] + " " + packageEventValue(request[1], request, 3)));
        } else if (command.equals("routeRemote")) {
            Message message = new Message(request[2] + " " + packageEventValue(request[1], request, 3));
            setChanged();
            notifyObservers(message);
        } else if (command.equals("function")) {
            dispatcher.dispatch(new Request(getClassName() + " " + getId(), request[1], request[3] + " " + packageEventValue(request[2], request, 4)));
        } else if (command.equals("return")) {
            Logger.logDebug((String) functionReturnTargets.get(request[1]) + " " + packageEventValue(request[2], request, 3));
            dispatcher.dispatch(new Message((String) functionReturnTargets.get(request[1]) + " " + packageEventValue(request[2], request, 3)));
            functionReturnTargets.remove(request[1]);
        } else if (command.equals("declareEvents")) {
            String[] events = new String[request.length - 1];
            for (int i = 0; i < request.length - 1; i++) {
                events[i] = request[i + 1];
            }
            handleDeclareEvents(events);
            flush();
        }
    }
