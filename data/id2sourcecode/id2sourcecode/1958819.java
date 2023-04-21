    @SuppressWarnings("unchecked")
    public static final <T> T[] toReversed(final T[] array, final int offset, final int length) {
        return length < 0 ? Jadoth.reverseArraycopy(array, offset, (T[]) Array.newInstance(array.getClass().getComponentType(), -length), 0, -length) : Jadoth.reverseArraycopy(array, offset + length - 1, (T[]) Array.newInstance(array.getClass().getComponentType(), length), 0, length);
    }
