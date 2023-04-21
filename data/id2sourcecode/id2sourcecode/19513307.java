            public void run() {
                connectionIdTL.set(connectionId);
                try {
                    final Class<?> targetIF = message.getIfClass();
                    final Object targetIFImpl = localInterfaces.get(targetIF);
                    if (targetIFImpl != null) {
                        final Method method = targetIF.getMethod(message.methodName, message.parameterTypes);
                        if (method == null) throw new IllegalArgumentException("Interface method does not exist");
                        final Object resp = method.invoke(targetIFImpl, message.getParameters());
                        if (!message.asyncCall) writeMessage(new RPCCallRespMessage(message.threadId, true, resp));
                    } else throw new IllegalArgumentException("Interface not supported");
                } catch (final Exception e) {
                    final Throwable cause = e instanceof InvocationTargetException ? e.getCause() : e;
                    if (!message.asyncCall) writeMessage(new RPCCallRespMessage(message.threadId, false, cause)); else logger.warn("Exception caught whilst processing async call to " + message.ifClass + "." + message.methodName + "()", e);
                }
                connectionIdTL.set(null);
            }
