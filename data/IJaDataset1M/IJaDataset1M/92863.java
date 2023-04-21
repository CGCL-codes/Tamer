package org.kadosu.archive;

import java.util.Collection;
import org.kadosu.exception.KDSAccessException;

/**
 * This is the interface for your archive implementation.
 */
public interface IArchive {

    /**
   * Returns the content of the archive without the path. If there is no content
   * it should return an empty Collection.
   * 
   * @return The document or archive names.
   */
    public Collection getContent();

    /**
   * Sets the archive to inspect.
   * 
   * @param path
   * @throws KDSAccessException
   */
    public void setArchive(String path) throws KDSAccessException;

    /**
   * Returns the location of the extracted content.
   * 
   * @return A Path.
   */
    public String getPath();
}
