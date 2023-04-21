    public void mergeWith(MethodParams withThisNewOnes, boolean overwrite) {
        RunTime.assumedNotNull(withThisNewOnes, overwrite);
        if (this == withThisNewOnes) {
            RunTime.badCall("attempted to merge with self");
        }
        Iterator<Entry<ParamID, Reference<Object>>> iter = withThisNewOnes.getIter();
        while (iter.hasNext()) {
            Entry<ParamID, Reference<Object>> current = iter.next();
            ParamID paramName = current.getKey();
            Reference<Object> refToValue = current.getValue();
            boolean alreadyExists = this.get(paramName) != null;
            if ((alreadyExists && overwrite) || (!alreadyExists)) {
                this.set(paramName, refToValue.getObject());
                RunTime.assumedTrue(this.getEx(paramName) == withThisNewOnes.getEx(paramName));
                RunTime.assumedTrue(this.get(paramName) != withThisNewOnes.get(paramName));
            }
        }
    }
