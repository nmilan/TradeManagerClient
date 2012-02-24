package custom.forms.payment;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Locale;

import generic.form.FormType;
import generic.form.GenericForm;
import generic.form.GenericFormToolbar;
import generic.form.GenericInputFormI;
import generic.tools.MessageObject;
import hibernate.entityBeans.SalesPrice;
import hibernate.entityBeans.StockDocument;

import javax.swing.JPanel;
import javax.swing.JTextField;

import remotes.RemotesManager;

import com.toedter.calendar.JDateChooser;

import custom.forms.payment.observer.OkEvent;
import custom.forms.payment.observer.OkListener;
import custom.forms.payment.observer.OkObservable;

import layouts.RiverLayout;
import localization.Local;
import model.metadata.EntityMetadata;

import actions.generic.textfieldValidators.DoubleValidator;
import actions.generic.textfieldValidators.IntegerValidator;

import util.DateUtil;
import util.ServerResponse;

public class PaymentForm extends JPanel implements OkObservable{
	
	private InputFilterForm inputFilterForm = null;
	private JPanel leftPanel = null;
	private JPanel rightPanel = null;
	private ArrayList<OkListener> okListeners;
	private PaymentInputForm paymentInputForm;
	private EntityMetadata em;
	private static PaymentForm instance = null;
	
	public PaymentForm() {
		okListeners = new ArrayList<OkListener>();
		initComponents();
		addComponents();
	}

	private void addComponents() {
		leftPanel = new JPanel();
		leftPanel.setLayout(new RiverLayout());
		rightPanel = new JPanel();
		rightPanel.setLayout(new RiverLayout());
		
		inputFilterForm = new InputFilterForm(this);
		paymentInputForm = new PaymentInputForm(this);
		
		setLayout(new BorderLayout());
		
		leftPanel.add("vfill hfill", inputFilterForm);
		rightPanel.add("vfill hfill", paymentInputForm);
		add(leftPanel,BorderLayout.CENTER);
		add(rightPanel,BorderLayout.EAST);
		
	}
	private void initComponents() {
		
	}

	public static JTextField getTextField(String dataType, boolean enabled) {
		JTextField tf = new JTextField(25);
		tf.setEnabled(enabled);
		if (dataType.equals("Integer")) {
			tf.setDocument(new IntegerValidator());
			tf.setHorizontalAlignment(JTextField.RIGHT);
		} else if (dataType.equals("Double")) {
			tf.setDocument(new DoubleValidator());
			tf.setHorizontalAlignment(JTextField.RIGHT);
		}
		return tf;
	}

	@Override
	public void addlistener(OkListener listener) {
		okListeners.add(listener);
	}

	@Override
	public void removeListener(OkListener listener) {
		okListeners.remove(listener);
		
	}

	@Override
	public void fireOkPerformed(OkEvent event) {
		if (event == null)
			return;
		ArrayList<OkListener> temp;
		synchronized (this) {
			temp = (ArrayList<OkListener>) okListeners.clone();
		}

		for (OkListener s : temp)
			s.okPerformed(event);
		
		
	}
	public JDateChooser getDateChooser(){
		JDateChooser chooser = new JDateChooser();
		chooser.setLocale(new Locale(Local.getLocale()));
		chooser.getJCalendar().setWeekOfYearVisible(false);
		chooser.setDateFormatString(DateUtil.DATE_FORMAT);
		chooser.setSize(350, 40);
		chooser.setPreferredSize(new Dimension(350, 40));
		return chooser;
	}

	public static PaymentForm getInstance() {
		if (instance == null) {
			instance = new PaymentForm();
		}
		
		((StockDocumentTableModel)instance.inputFilterForm.getStockDocumentTable().getModel()).getStockDocuments().clear();
		((PaymentTableModel) instance.paymentInputForm.getPaymentTable().getModel()).getStockDocumentsPayment().clear();
		instance.paymentInputForm.inputState();
		instance.inputFilterForm.clear();
		return instance;
	}

	public InputFilterForm getInputFilterForm() {
		return inputFilterForm;
	}

	public void setInputFilterForm(InputFilterForm inputFilterForm) {
		this.inputFilterForm = inputFilterForm;
	}

	public PaymentInputForm getPaymentInputForm() {
		return paymentInputForm;
	}

	public void setPaymentInputForm(PaymentInputForm paymentInputForm) {
		this.paymentInputForm = paymentInputForm;
	}
	
}
