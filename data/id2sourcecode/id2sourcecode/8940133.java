    @SuppressWarnings("unchecked")
    public Comparable<?> getResult(Comparable<?>... comparables) throws ParseException {
        String returnColumnName = null;
        returnColumnName = comparables[0].toString().toLowerCase();
        Map<String, Object> result = null;
        Comparable<?>[] parameters = new Comparable<?>[comparables.length - 1];
        if (isThreadLocalCache()) {
            int threadLocalKey = this.hashCode();
            if (parameterSize > 1) {
                int hash = this.hashCode();
                for (int i = 0; i < parameters.length; i++) {
                    parameters[i] = comparables[i + 1];
                    hash ^= parameters[i].hashCode() << (i + 1);
                }
                threadLocalKey = threadLocalKey ^ hash;
            }
            result = (Map<String, Object>) ThreadLocalMap.get(threadLocalKey);
            if (result == null) {
                if (!ThreadLocalMap.containsKey(threadLocalKey)) {
                    result = query(parameters);
                    ThreadLocalMap.put(threadLocalKey, result);
                }
            }
        } else {
            result = query(parameters);
        }
        if (result == null) {
            return (null);
        } else {
            return ((Comparable<?>) result.get(returnColumnName));
        }
    }
