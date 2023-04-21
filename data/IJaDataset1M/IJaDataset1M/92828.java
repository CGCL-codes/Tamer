package org.jboss.netty.handler.codec.http2;

/**
 * FileUpload interface that could be in memory, on temporary file or any other implementations.
 *
 * Most methods are inspired from java.io.File API.
 *
 * @author <a href="http://www.jboss.org/netty/">The Netty Project</a>
 * @author Andy Taylor (andy.taylor@jboss.org)
 * @author <a href="http://gleamynode.net/">Trustin Lee</a>
 * @author <a href="http://openr66.free.fr/">Frederic Bregier</a>
 *
 */
public interface FileUpload extends HttpData {

    /**
     * Returns the original filename in the client's filesystem,
     * as provided by the browser (or other client software).
     * @return the original filename
     */
    public String getFilename();

    /**
     * Set the original filename
     * @param filename
     */
    public void setFilename(String filename);

    /**
     * Set the Content Type passed by the browser if defined
     * @param contentType Content Type to set - must be not null
     */
    public void setContentType(String contentType);

    /**
     * Returns the content type passed by the browser or null if not defined.
     * @return the content type passed by the browser or null if not defined.
     */
    public String getContentType();

    /**
     * Set the Content-Transfer-Encoding type from String as 7bit, 8bit or binary
     * @param contentTransferEncoding
     */
    public void setContentTransferEncoding(String contentTransferEncoding);

    /**
     * Returns the Content-Transfer-Encoding
     * @return the Content-Transfer-Encoding
     */
    public String getContentTransferEncoding();
}
