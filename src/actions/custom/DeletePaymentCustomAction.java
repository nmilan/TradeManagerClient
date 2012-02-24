package actions.custom;

import generic.MessagePanel;
import generic.form.FormStateEnum;
import generic.form.FormType;
import generic.form.GenericForm;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import custom.forms.payment.PaymentForm;

import localization.Local;
import actions.CustomAbstractAction;
import app.Appliction;

public class DeletePaymentCustomAction  extends CustomAbstractAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4230488905073808669L;

	public DeletePaymentCustomAction() {
		KeyStroke ks = KeyStroke.getKeyStroke((char) KeyEvent.VK_DELETE);
		putValue(ACCELERATOR_KEY, ks);
		putValue(NAME, Local.getString("DELETE"));
		putValue(SHORT_DESCRIPTION, Local.getString("DELETE_ACTION_DESCRIPTION"));
		putValue(LARGE_ICON_KEY, loadIcon("delete.png"));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		PaymentForm paif = Appliction.getInstance().getMainFrame().getCurrentCustomForm();
		if (!paif.getPaymentInputForm().removeValidate()) {
			return;
		}
		Object[] options = {Local.getString("YES"),
				Local.getString("NO")};
		int result = JOptionPane.showOptionDialog(Appliction.getInstance().getMainFrame(), Local.getString("DELETE_YES_NO_QUESTION"), Local.getString("CONFIRMATION"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

		if (result == JOptionPane.YES_OPTION){
			
			paif.getPaymentInputForm().removeEntity();
		}
	}
}