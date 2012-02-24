package actions.generic;

import generic.form.FormStateEnum;
import generic.form.GenericForm;
import hibernate.entityBeans.StockDocument;

import java.awt.event.ActionEvent;

import javax.swing.Icon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import custom.forms.CustomFormsFactory;
import custom.forms.payment.InputFilterForm;
import custom.forms.payment.PaymentForm;

import localization.Local;
import actions.CustomAbstractAction;
import app.Appliction;

public class ViewPaymentAction extends CustomAbstractAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7302160095473095571L;
	
	public ViewPaymentAction() {
		putValue(NAME, Local.getString("VIEWPAYMENT"));
		putValue(SHORT_DESCRIPTION, Local.getString("PAYMENT_ACTION_DESCRIPTION"));
	}	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Appliction.getInstance().getMainFrame().addNewTabPanel(Local.getString("VIEWPAYMENT"), PaymentForm.getInstance());
	}
}