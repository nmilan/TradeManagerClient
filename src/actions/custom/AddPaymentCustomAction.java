package actions.custom;


import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import custom.forms.payment.PaymentForm;

import localization.Local;
import actions.CustomAbstractAction;
import app.Appliction;

public class AddPaymentCustomAction  extends CustomAbstractAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9086023992034936436L;
	public AddPaymentCustomAction() {
		KeyStroke ks = KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK);
		putValue(ACCELERATOR_KEY, ks);
		putValue(NAME, Local.getString("ADD"));
		putValue(SHORT_DESCRIPTION, Local.getString("ADD_ACTION_DESCRIPTION"));
		putValue(LARGE_ICON_KEY, loadIcon("add.png"));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		PaymentForm paif = Appliction.getInstance().getMainFrame().getCurrentCustomForm();
		paif.getPaymentInputForm().inputState();
	}
}
