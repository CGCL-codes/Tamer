    public Object invoke(Remote obj, Method method, Object[] params, long opnum) throws Exception {
        if (clientRefLog.isLoggable(Log.VERBOSE)) {
            clientRefLog.log(Log.VERBOSE, "method: " + method);
        }
        if (clientCallLog.isLoggable(Log.VERBOSE)) {
            logClientCall(obj, method);
        }
        Connection conn = ref.getChannel().newConnection();
        RemoteCall call = null;
        boolean reuse = true;
        boolean alreadyFreed = false;
        try {
            if (clientRefLog.isLoggable(Log.VERBOSE)) {
                clientRefLog.log(Log.VERBOSE, "opnum = " + opnum);
            }
            call = new StreamRemoteCall(conn, ref.getObjID(), -1, opnum);
            try {
                ObjectOutput out = call.getOutputStream();
                marshalCustomCallData(out);
                Class<?>[] types = method.getParameterTypes();
                for (int i = 0; i < types.length; i++) {
                    marshalValue(types[i], params[i], out);
                }
            } catch (IOException e) {
                clientRefLog.log(Log.BRIEF, "IOException marshalling arguments: ", e);
                throw new MarshalException("error marshalling arguments", e);
            }
            call.executeCall();
            try {
                Class<?> rtype = method.getReturnType();
                if (rtype == void.class) return null;
                ObjectInput in = call.getInputStream();
                Object returnValue = unmarshalValue(rtype, in);
                alreadyFreed = true;
                clientRefLog.log(Log.BRIEF, "free connection (reuse = true)");
                ref.getChannel().free(conn, true);
                return returnValue;
            } catch (IOException e) {
                clientRefLog.log(Log.BRIEF, "IOException unmarshalling return: ", e);
                throw new UnmarshalException("error unmarshalling return", e);
            } catch (ClassNotFoundException e) {
                clientRefLog.log(Log.BRIEF, "ClassNotFoundException unmarshalling return: ", e);
                throw new UnmarshalException("error unmarshalling return", e);
            } finally {
                try {
                    call.done();
                } catch (IOException e) {
                    reuse = false;
                }
            }
        } catch (RuntimeException e) {
            if ((call == null) || (((StreamRemoteCall) call).getServerException() != e)) {
                reuse = false;
            }
            throw e;
        } catch (RemoteException e) {
            reuse = false;
            throw e;
        } catch (Error e) {
            reuse = false;
            throw e;
        } finally {
            if (!alreadyFreed) {
                if (clientRefLog.isLoggable(Log.BRIEF)) {
                    clientRefLog.log(Log.BRIEF, "free connection (reuse = " + reuse + ")");
                }
                ref.getChannel().free(conn, reuse);
            }
        }
    }
