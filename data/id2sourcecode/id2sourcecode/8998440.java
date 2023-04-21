    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getDeclaringClass() == Object.class) {
            try {
                return method.invoke(realObject, args);
            } catch (InvocationTargetException ite) {
                throw ite.getCause();
            }
        }
        int remainingRetries = numRetries;
        int waitTime = minWait + rng.nextInt(maxWait - minWait);
        Throwable thrown = null;
        while (remainingRetries >= 0) {
            try {
                return method.invoke(realObject, args);
            } catch (InvocationTargetException ite) {
                thrown = ite.getCause();
                if (remainingRetries == 0) {
                    log.info("Reached maximum retry count, giving up.");
                    break;
                } else {
                    if (retryExceptionTypes != null && retryExceptionTypes.length > 0) {
                        boolean shouldRetry = false;
                        for (Class<? extends Throwable> exceptionType : retryExceptionTypes) {
                            if (exceptionType.isInstance(thrown)) {
                                shouldRetry = true;
                                break;
                            }
                        }
                        if (!shouldRetry) {
                            log.info("Call threw exception of type " + thrown.getClass().getName() + ", which is not configured to cause a retry - giving up.");
                            break;
                        }
                    }
                    log.debug("Exception thrown by method " + method.getName() + " of " + realObject + ". Retrying in " + waitTime + "ms", thrown);
                    try {
                        Thread.sleep(waitTime);
                    } catch (InterruptedException ie) {
                        log.info("Thread interrupted, giving up retrying.");
                        throw thrown;
                    }
                    remainingRetries--;
                    waitTime = (int) (waitTime * backoffMultiplier);
                }
            }
        }
        throw thrown;
    }
