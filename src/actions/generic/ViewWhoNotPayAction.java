package actions.generic;

import java.awt.event.ActionEvent;

import localization.Local;
import actions.CustomAbstractAction;
import app.Appliction;
import custom.forms.payment.PaymentForm;
import custom.forms.payment.WhoNotPay;

public class ViewWhoNotPayAction extends CustomAbstractAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7302160095473095571L;
	
	public ViewWhoNotPayAction() {
		putValue(NAME, Local.getString("WHONOTPAY"));
		putValue(SHORT_DESCRIPTION, Local.getString("WHONOTPAY"));
	}	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Appliction.getInstance().getMainFrame().addNewTabPanel(Local.getString("WHONOTPAY"), WhoNotPay.getInstance());
	}
}
