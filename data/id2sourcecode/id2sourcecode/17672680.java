        private Object Flush(Object[] args) {
            Args.massage(FunctionSpec.noParams, args);
            try {
                f.getChannel().force(true);
            } catch (IOException e) {
                throw new SuException("File Flush failed", e);
            }
            return null;
        }
