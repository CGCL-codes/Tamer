        @Override
        protected Object getObject(Object o, int id) {
            switch(id) {
                case Type.SUPER_FIELD_COUNT + 0:
                    return ((SpectralAreaLight) o).getChannel();
            }
            return super.getObject(o, id);
        }
