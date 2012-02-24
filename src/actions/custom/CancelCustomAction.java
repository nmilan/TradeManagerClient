package actions.custom;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import actions.CustomAbstractAction;
import app.Appliction;
import custom.forms.payment.InputFilterForm;
import custom.forms.payment.PaymentForm;
import custom.forms.payment.PaymentInputForm;
import custom.forms.payment.PaymentTableModel;

import localization.Local;

public class CancelCustomAction extends CustomAbstractAction{
	
	public CancelCustomAction() {
		KeyStroke ks = KeyStroke.getKeyStroke((char) KeyEvent.VK_ESCAPE);
		putValue(ACCELERATOR_KEY, ks);
		putValue(NAME, Local.getString("CANCEL"));
		putValue(SHORT_DESCRIPTION, Local.getString("CANCEL_ACTION_DESCRIPTION"));
		putValue(LARGE_ICON_KEY, loadIcon("no.png"));
	}	
	@Override
	public void actionPerformed(ActionEvent e) {
		PaymentForm pay = Appliction.getInstance().getMainFrame().getCurrentCustomForm();
		InputFilterForm inf = pay.getInputFilterForm();
		inf.clear();
		PaymentInputForm inp = pay.getPaymentInputForm();
		inp.clearAll();
		
	}

}
