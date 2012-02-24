package custom.forms.payment.observer;

import hibernate.entityBeans.StockDocument;

import java.util.Date;
import java.util.EventObject;

public class OkEvent extends EventObject{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Object object;
	public OkEvent(Object source,Object object) {
		super(source);
		this.object = object;
	}
	public Object getObject() {
		return object;
	}
	public void setObject(Object object) {
		this.object = object;
	}
	

}
