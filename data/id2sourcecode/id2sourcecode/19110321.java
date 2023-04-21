        public Object invoke(final Object bean, final Method readMethod, final Method writeMethod) {
            try {
                return readMethod.invoke(bean);
            } catch (final Exception e) {
                throw BeansOpeException.wrapException(e);
            }
        }
