package com.bluebrim.browser.client;

import java.awt.Font;
import javax.swing.JList;

/**
 * Insert the type's description here.
 * Creation date: (2001-04-04 17:43:59)
 * @author: 
 */
public abstract class CoImmutableAsItalicCatalogListCellRenderer extends CoCatalogListCellRenderer {

    private Font m_font;

    private Font m_immutableFont;

    /**
 * CoImmutableAsItalicCatalogListCellRenderer constructor comment.
 */
    protected CoImmutableAsItalicCatalogListCellRenderer() {
        super();
    }

    /**
 * CoImmutableAsItalicCatalogListCellRenderer constructor comment.
 * @param hTextPosition int
 * @param vTextPosition int
 * @param hAlignment int
 * @param defaults com.bluebrim.base.client.CoUIDefaults
 */
    public CoImmutableAsItalicCatalogListCellRenderer(int hTextPosition, int vTextPosition, int hAlignment) {
        super(hTextPosition, vTextPosition, hAlignment);
    }

    /**
 * CoImmutableAsItalicCatalogListCellRenderer constructor comment.
 * @param hTextPosition int
 * @param vTextPosition int
 * @param defaults com.bluebrim.base.client.CoUIDefaults
 */
    public CoImmutableAsItalicCatalogListCellRenderer(int hTextPosition, int vTextPosition) {
        super(hTextPosition, vTextPosition);
    }

    protected abstract boolean isMutable(Object value, int index);

    protected void setColorAndFont(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.setColorAndFont(list, value, index, isSelected, cellHasFocus);
        if (!isMutable(value, index)) {
            if ((m_font == null) || !list.getFont().equals(m_font)) {
                m_font = list.getFont();
                m_immutableFont = m_font.deriveFont(Font.ITALIC);
            }
            setFont(m_immutableFont);
        }
    }
}
