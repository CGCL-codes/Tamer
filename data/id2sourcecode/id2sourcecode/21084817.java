        public void setWriteMethod(Method writeMethod) {
            this.writeMethod = writeMethod;
            if (writeMethod != null) {
                Class<?>[] args = writeMethod.getParameterTypes();
                if (args.length != 1) {
                    LOG.error("invalid argument count for writeMethod");
                }
                Class<?>[] params = writeMethod.getParameterTypes();
                if (this.propertyType == null) this.propertyType = params[0]; else if (this.propertyType != params[0]) {
                    LOG.error("type mismatch between writeMethod and readMethod");
                }
            } else {
                this.propertyType = null;
            }
        }
