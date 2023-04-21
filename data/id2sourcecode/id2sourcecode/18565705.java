        @Override
        public LispObject execute(LispObject arg) throws ConditionThrowable {
            if (arg instanceof Mutex) try {
                ((Mutex) arg).acquire();
                return T;
            } catch (InterruptedException e) {
                return error(new LispError("The thread " + LispThread.currentThread().writeToString() + " was interrupted."));
            }
            return error(new TypeError("The value " + arg.writeToString() + " is not a mutex."));
        }
