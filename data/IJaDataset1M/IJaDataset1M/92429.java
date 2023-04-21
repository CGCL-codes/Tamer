package de.ios.kontor.sv.order.impl;

import java.math.*;
import java.rmi.*;
import de.ios.framework.db2.*;
import de.ios.framework.basic.*;
import de.ios.kontor.utils.*;
import de.ios.kontor.sv.order.co.*;
import de.ios.kontor.sv.address.co.Customer;
import de.ios.kontor.sv.address.impl.CustomerDBO;
import de.ios.kontor.sv.basic.impl.*;

/**
 * Implements the ArticleType-Interface, encapsulating a ArticleTypeDBO-Object.
 */
public class ArticleTypeImpl extends BasicImpl implements ArticleType {

    /** The object. */
    protected ArticleTypeDBO dbo = null;

    /**
   * Modify the ArticleType.
   * @param _number the Article-Number.
   * @param _price the Price of this Article-Type.
   * @param _VATLevel the objectid of the VAT-Level.
   * @param _saleUnit The sale unit for this Article-Type.
   * @param _description the Description for Invoices and Delivery-Notes.
   * @param _name the (Short-)Name of this Article-Type.
   * @param _unit the Unit for this Article-Type (pieces, meters, liters, ...).
   * @param _dutyPerc the percentage of duty for foreign Article-Types.
   * @param _endDate the End-Date for this Article-Type up to which this Article is offered.
   * @param _customer the Customer, to which this Article-Type is only offered (if not 'null').
   * @param _onlySubArticle If true, ArticleType is only a Sub-At.T. of another A.T.
   * @exception de.ios.kontor.utils.KontorException if the modification of the ArticleType failed.
   */
    public void modify(String _number, BigDecimal _price, Long _VATLevel, Integer _saleUnit, String _description, String _name, String _unit, BigDecimal _dutyPerc, IoSDate _endDate, Customer _customer, Boolean _onlySubArticle) throws KontorException {
        try {
            long coid = (_customer == null) ? (-1) : ((CustomerDBO) _customer.getDBObject()).getOId();
            dbo.setAll(_number, _price, _VATLevel, _saleUnit, _description, _name, _unit, _dutyPerc, _endDate, (coid > 0) ? (new Long(coid)) : (null), _onlySubArticle, true);
        } catch (Throwable t) {
            throw new KontorException("Modification of an ArticleType failed.", t);
        }
    }

    /**
   * Constructor for a certain DBObject.
   * @param _db a DBObjectServer.
   * @param _sc the SessionCarrier.
   * @param _dbo the DBObject for that Class.
   * @exception java.rmi.RemoteException if the connection to the Server failed.
   */
    public ArticleTypeImpl(DBObjectServer _db, SessionCarrier _sc, ArticleTypeDBO _dbo) throws RemoteException {
        super(_db, _sc, _dbo);
        dbo = _dbo;
    }

    /**
   * Get the ControllerImpl for this Business-Object.
   * Use this Method only, if you implement Methods requiring the Controller at basic Classes.
   * You may cache the Controller, but never cache the Session!
   * @return the ControllerImpl for this Object.
   * @exception de.ios.kontor.utils.KontorException if the Operation failed due to a Server-Error.
   */
    public BasicControllerImpl getBasicControllerImpl() throws KontorException {
        return getSessionImpl().getArticleTypeControllerImpl();
    }

    /**
   * @return the Article-Number for that Article-Type
   */
    public String getNumber() {
        return dbo.number.get();
    }

    /**
   * Return the price of this Article. If the given Customer is not null,
   * the price for the price category the Customer is in is returned.
   * If the Customer is in no price category, the "normal" Article price
   * is returned.
   *
   * @param c A Customer.
   *
   * @return the Price of that Article.
   * @exception RemoteException thrown if connection to the Server failed.
   */
    public BigDecimal getPrice(Customer c) throws RemoteException, KontorException {
        try {
            if (c == null) return dbo.price.get(); else {
                if (c.getPriceCategoryOId() == null) return dbo.price.get(); else {
                    Iterator it = getSessionImpl().getSpecialPriceControllerImpl().getSpecialPriceDC(c.getPriceCategoryOId(), new Long(getObjectId()));
                    if ((it != null) && (it.size() != 0)) {
                        it.next();
                        SpecialPriceDC s = (SpecialPriceDC) it.getObject();
                        return s.price;
                    } else {
                        return dbo.price.get();
                    }
                }
            }
        } catch (Throwable t) {
            throw new KontorException("Error while getting price for Article!", t);
        }
    }

    /**
   * set the price of the ArticleType
   * @param p price
   */
    public void setPrice(BigDecimal p) {
        dbo.price.set(p);
    }

    /**
   * @return the objectid of the VAT level.
   */
    public Long getVATLevel() {
        return dbo.VATLevel.getLong();
    }

    /**
   * set the objectid of the VAT level for this Article.
   * @param v the VAT level
   * @exception RemoteException thrown if connection to the Server failed.
   */
    public void setVATLevel(Long v) {
        dbo.VATLevel.set(v);
    }

    /**
   * @return The sale unit for this Article-Type.
   * @exception RemoteException thrown if connection to the Server failed.
   */
    public Integer getSaleUnit() {
        return dbo.saleUnit.getInt();
    }

    /**
   * Set the sale unit for this Article-Type.
   * @param s The sale unit for this Article-Type.
   * @exception RemoteException thrown if connection to the Server failed.
   */
    public void setSaleUnit(Integer s) {
        dbo.saleUnit.set(s);
    }

    /**
   * @return the description (multiline string) for DeliveryNotes and Invoices
   */
    public String getDescription() {
        return dbo.description.get();
    }

    /**
   * Set the number.
   * @param n The number.
   */
    public void setNumber(String n) {
        dbo.number.set(n);
    }

    /**
   * Set the description.
   * @param d The description.
   */
    public void setDescription(String d) {
        dbo.description.set(d);
    }

    /**
   * @return the Name of this Article-Type.
   */
    public String getName() {
        return dbo.name.get();
    }

    /**
   * set the Name of this Article-Type.
   * @param n the name
   * @exception RemoteException thrown if connection to the Server failed.
   */
    public void setName(String n) {
        dbo.name.set(n);
    }

    /**
   * Get the percentage of duty for foreign Article-Types
   * @return the percentage of duty for foreign Article-Types.
   */
    public BigDecimal getDutyPerc() {
        return dbo.dutyPerc.get();
    }

    /**
   * Get the percentage of duty for foreign Article-Types
   * @param dutyPerc the percentage of duty for foreign Article-Types.
   */
    public void setDutyPerc(BigDecimal dutyPerc) {
        dbo.dutyPerc.set(dutyPerc);
    }

    /**
   * @return the End-Date of this Article-Type up to which it is offered.
   */
    public IoSDate getEndDate() {
        return dbo.endDate.get();
    }

    /**
   * Set the End-Date of this Article-Type up to which it is offered.
   * @param d the end date
   * @exception RemoteException thrown if connection to the Server failed.
   */
    public void setEndDate(IoSDate d) {
        dbo.endDate.set(d);
    }

    /**
   * @return the Unit for this Article-Type.
   */
    public String getUnit() {
        return dbo.unit.get();
    }

    /**
   * set the Unit for this Article-Type.
   * @param u the unit
   * @exception RemoteException thrown if connection to the Server failed.
   */
    public void setUnit(String u) {
        dbo.unit.set(u);
    }

    /**
   * Get the customer objectid.
   */
    public Long getCustomerOId() {
        return dbo.customerOId.getLong();
    }

    /**
   * Set the Customer objectid from the given Customer.
   * @param c the customer 
   */
    public void setCustomer(Customer c) throws RemoteException {
        long coid = (c == null) ? (-1) : ((CustomerDBO) c.getDBObject()).getOId();
        dbo.customerOId.set((coid == -1) ? null : new Long(coid));
    }

    /**
   * @return true, if ArticleType is only a sub article
   */
    public Boolean getOnlySubArticle() throws RemoteException {
        return dbo.onlySubArticle.getBoolean();
    }

    /**
   * @param b true, if ArticleType is only a sub article
   */
    public void setOnlySubArticle(Boolean b) throws RemoteException {
        dbo.onlySubArticle.set(b);
    }

    /**
   * Get the weight of this ArticleType.
   * @param Number of pieces, may be null
   * @return Article weight
   */
    public BigDecimal getWeight(Long numberOfPieces) throws RemoteException, KontorException {
        throw new KontorException("Not yet implemented: ArticleType weight!");
    }
}
