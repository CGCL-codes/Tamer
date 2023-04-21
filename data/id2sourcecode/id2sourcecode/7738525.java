            public IOServiceSpec(String modelName, ModelReader<? extends Model> reader, ModelWriter<? extends Model> writer) {
                this.modelName = modelName;
                this.reader = reader;
                this.writer = writer;
            }
