            public Object getValue(Object object) throws IllegalStateException {
                Content target = (Content) object;
                return target.getChannel();
            }
