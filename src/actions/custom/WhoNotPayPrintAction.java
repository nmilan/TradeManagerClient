package actions.custom;

import java.awt.event.ActionEvent;

import custom.forms.payment.WhoNotPay;

import localization.Local;
import actions.CustomAbstractAction;
import app.Appliction;

public class WhoNotPayPrintAction extends CustomAbstractAction{
	
	public WhoNotPayPrintAction() {
		//putValue(NAME, Local.getString("PRINT"));
		putValue(SHORT_DESCRIPTION, Local.getString("PRINT_LIST_ACTION_DESCRIPTION"));
		putValue(LARGE_ICON_KEY, loadIcon("print.png"));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		WhoNotPay whoNotPay = WhoNotPay.getInstance();
		whoNotPay.print();
	}
}
