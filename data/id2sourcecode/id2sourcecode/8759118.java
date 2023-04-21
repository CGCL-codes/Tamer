                    @Override
                    public Object invoke(final Object bean, final Method readMethod, final Method writeMethod) {
                        try {
                            final Object vObject = readMethod.invoke(bean);
                            if (ID.class.isAssignableFrom(readMethod.getReturnType()) && vObject == null) {
                                return table.getNullId();
                            } else {
                                return vObject;
                            }
                        } catch (final Exception e) {
                            throw BeansOpeException.wrapException(e);
                        }
                    }
