package actions.custom;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import actions.CustomAbstractAction;
import app.Appliction;

import custom.forms.payment.InputFilterForm;
import custom.forms.payment.PaymentForm;

import localization.Local;

public class OkCustomAction extends CustomAbstractAction{
	
	public OkCustomAction() {
		KeyStroke ks = KeyStroke.getKeyStroke(KeyEvent.VK_S,ActionEvent.CTRL_MASK);
		putValue(ACCELERATOR_KEY, ks);
		putValue(NAME, Local.getString("SAVE"));
		putValue(SHORT_DESCRIPTION, Local.getString("SAVE_ACTION_DESCRIPTION"));
		putValue(LARGE_ICON_KEY, loadIcon("ok.png"));
	}	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		PaymentForm pay = Appliction.getInstance().getMainFrame().getCurrentCustomForm();
		InputFilterForm inf = pay.getInputFilterForm();
		inf.searchStockDocument();
		
	}
}

