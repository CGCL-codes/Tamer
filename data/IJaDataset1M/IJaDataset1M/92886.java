package org.nomicron.suber.model.dao;

import org.nomicron.suber.model.object.Keyword;
import com.dreamlizard.miles.interfaces.Dao;
import java.util.List;

/**
 * Data access object interface for Keyword.
 */
public interface KeywordDao extends Dao {

    /**
     * Get a new instance of a Keyword.
     *
     * @return new Keyword object
     */
    Keyword create();

    /**
     * Store the specified Keyword to the persistence layer.
     *
     * @param object Keyword to store
     */
    void store(Keyword object);

    /**
     * Find the Keyword with the specified id.
     *
     * @param id id to look for
     * @return matching Keyword
     */
    Keyword findById(Integer id);

    /**
     * Find the List of all Keywords.
     *
     * @return List of Keywords
     */
    List<Keyword> findAll();
}
