        public void setReadMethod(Method readMethod) {
            this.readMethod = readMethod;
            if (this.readMethod != null) {
                Class<?>[] args = readMethod.getParameterTypes();
                if (args.length != 0) {
                    LOG.error("invalid argument count for readMethod");
                }
                if (this.propertyType == null) this.propertyType = readMethod.getReturnType(); else if (this.propertyType != readMethod.getReturnType()) {
                    LOG.error("type mismatch between writeMethod and readMethod");
                }
            } else {
                this.propertyType = null;
            }
        }
