package net.sourceforge.seqware.common.model.lists;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import net.sourceforge.seqware.common.model.Processing;

/**
 *
 * @author mtaschuk
 */
public class ProcessingList {

    protected List<Processing> tList;

    public ProcessingList() {
        tList = new ArrayList<Processing>();
    }

    public List<Processing> getList() {
        return tList;
    }

    public void setList(List<Processing> list) {
        this.tList = list;
    }

    public void add(Processing ex) {
        tList.add(ex);
    }
}
