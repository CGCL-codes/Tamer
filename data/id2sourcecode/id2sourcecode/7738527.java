        void addServiceMapping(Class<? extends Model> cls, String typename, ModelReader<? extends Model> reader, ModelWriter<? extends Model> writer) {
            IOServiceSpec spec = new IOServiceSpec(typename, reader, writer);
            classToSpec.put(cls, spec);
            typenameToSpec.put(typename, spec);
        }
