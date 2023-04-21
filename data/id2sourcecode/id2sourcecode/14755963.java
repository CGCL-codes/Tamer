         * @throws java.io.IOException

         */
    private native long createNativeStream(OutputStream os, int iFormat, long hint) throws IOException;

    /**

         * Sets the CAB file comment. Not supported.

         * @param comment the comment string

         * @exception IllegalArgumentException if the length of the specified

         *		  ZIP file comment os greater than 0xFFFF bytes

         */
    @Override
    public void setComment(String comment) {
        if (null != comment) {
            throw new IllegalArgumentException("CAB does not support.");
        }
    }

    /**

         * Sets the default compression method for subsequent entries. This

         * default will be used whenever the compression method is not specified

         * for an individual CAB file entry, and is initially set to DEFLATED.

         * @param method the default compression method

         * @exception IllegalArgumentException if the specified compression method

         *		  is invalid

         */
