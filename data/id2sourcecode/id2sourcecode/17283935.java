    public static void main(final String[] args) {
        final BeanClassInfo inputInfo = BeanUtil.getBeanClassInfo(Machine.class);
        for (final PropertyDescriptor descriptor : inputInfo.propertyDescriptors) {
            final String name = descriptor.getName();
            System.out.println(" '" + name + "' : " + descriptor.getPropertyType() + "  (read: " + descriptor.getReadMethod() + ", write: " + descriptor.getWriteMethod() + ")");
        }
    }
