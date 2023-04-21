        BeanProperty(String name, Method readMethod, Method writeMethod, Class type) {
            if ((readMethod == null) && (writeMethod == null)) {
                throw new RuntimeException("La propiedad no se puede ni leer ni escribir");
            }
            _name = name;
            _readMethod = readMethod;
            if (_readMethod != null) {
                if (_readMethod.getParameterTypes().length > 0) {
                    _readMethod = null;
                }
            }
            _readMethod = readMethod;
            _writeMethod = writeMethod;
            if (_writeMethod != null) {
                if (_writeMethod.getParameterTypes().length > 1) {
                    _writeMethod = null;
                }
            }
            _type = type;
        }
