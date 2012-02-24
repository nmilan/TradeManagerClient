package custom.forms.payment;

import generic.tools.MessageObject;
import generic.tools.PopupManager;
import hibernate.entityBeans.StockDocument;
import hibernate.entityBeans.StockDocumentPayment;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;

import remotes.RemotesManager;

import util.ServerResponse;


import actions.ActionManager;

import com.toedter.calendar.JDateChooser;

import custom.forms.payment.observer.OkEvent;
import custom.forms.payment.observer.OkListener;
import custom.forms.stockdocuments.StockDocumentInputForm;

import layouts.RiverLayout;
import localization.Local;

public class PaymentInputForm extends JPanel implements OkListener{
	private PaymentTable paymentTable;
	private JPanel container = null;
	private JScrollPane scrollPane;
	private JToolBar toolBar;
	private PaymentForm paymentForm;
	private JLabel idLabel,amountLabel,dateLabel;
	private JTextField idTextfield,amountTextField;
	private JDateChooser dateChoser;
	private StockDocumentPayment stockDocumentPayment;
	private StockDocument stockDocument = null;
	
	public PaymentInputForm(PaymentForm paymentForm) {
		this.paymentForm = paymentForm;
		stockDocumentPayment = new StockDocumentPayment();
		paymentForm.addlistener(this);
		init();
		addComponents();
		
	}

	private void init() {
		setLayout(new BorderLayout());
		toolBar = new JToolBar();
		toolBar.add(ActionManager.getInstance().getAddPaymentCustomAction());
		toolBar.add(ActionManager.getInstance().getDeletePaymentCustomAction());
		toolBar.add(ActionManager.getInstance().getSaveCustomAction());
		toolBar.setEnabled(false);
		container = new JPanel();
		container.setLayout(new RiverLayout(0,0));
		paymentTable = new PaymentTable(paymentForm);
		scrollPane = new JScrollPane(paymentTable);
//		scrollPane.setPreferredSize(new Dimension(510,450));
		setBorder(BorderFactory.createTitledBorder(Local.getString("STOCKDOCUMENTPAYMENT")));
		container.add("vfill hfill",scrollPane);
		
		idLabel = new JLabel(Local.getString("STOCKDOCUMENTPAYMENT.ID"));
		amountLabel = new JLabel(Local.getString("STOCKDOCUMENTPAYMENT.AMOUNT"));
		dateLabel = new JLabel(Local.getString("STOCKDOCUMENTPAYMENT.PAYMENTDATE"));
		
		idTextfield =  StockDocumentInputForm.getTextField("INT", false);
		amountTextField =  StockDocumentInputForm.getTextField("Double", true);
		dateChoser = paymentForm.getDateChooser();
		dateChoser.setDate(new Date());
		
	}
	public void unbindData(){
		if(!idTextfield.getText().equals("")){
			int id;
			try {
				id = Integer.parseInt(idTextfield.getText());
				stockDocumentPayment.setID(id);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		double amout = 0;
		try {
			amout = Double.parseDouble(amountTextField.getText());
		} catch (Exception e) {
			// TODO: handle exception
		}
		stockDocumentPayment.setAmount(amout);
		stockDocumentPayment.setPaymentDate(dateChoser.getDate());
	}
	public void saveEntity(){
		if(stockDocument == null){
			return;
		}
		unbindData();
		MessageObject validateObject = validateData();
		if(validateObject.getSeverity()!=MessageObject.NONE){
			PopupManager.showMessage(validateObject, this);
			return;
		}
		ServerResponse response = null;
		unbindData();
		if(idTextfield.getText().equals("")){
			stockDocument.getPayments().add(stockDocumentPayment);
		}else{
			updatePayment(stockDocumentPayment);
		}
		stockDocument.setPaid(stockDocument.getPaidPay());
		response = RemotesManager.getInstance().getGenericPersistenceRemote().updateEntity(stockDocument);
		OkEvent event = new OkEvent(this, stockDocument);
		paymentForm.fireOkPerformed(event);
		
		
	}
	public void removeEntity(){
		if(stockDocument == null){
			return;
		}
		ServerResponse response = null;
		removePayment(stockDocumentPayment);
		stockDocument.setPaid(stockDocument.getPaidPay());
		response = RemotesManager.getInstance().getGenericPersistenceRemote().updateEntity(stockDocument);
		OkEvent event = new OkEvent(this, stockDocument);
		paymentForm.fireOkPerformed(event);
	}
	public void updatePayment(StockDocumentPayment stoPayment){
		for(StockDocumentPayment payment: stockDocument.getPayments()){
			if(stoPayment.getID().floatValue() == payment.getID().floatValue()){
				payment.setAmount(stoPayment.getAmount());
				payment.setPaymentDate(stoPayment.getPaymentDate());
			}
		}
	}
	public void removePayment(StockDocumentPayment stoPayment){
		for(StockDocumentPayment payment: stockDocument.getPayments()){
			if(stoPayment.getID().floatValue() == payment.getID().floatValue()){
				stockDocument.getPayments().remove(payment);
				return;
			}
		}
	}
	public MessageObject validateData(){
		MessageObject message = new MessageObject();
		message.setSeverity(MessageObject.NONE);
		
		if(stockDocumentPayment.getAmount()<=0){
			message.setMessage("Amount empty");
			message.setSeverity(MessageObject.WARN);
			message.setMessageCode("PAYMENT.AMOUNT_W");
			return message;
		}
		
		if(stockDocumentPayment.getPaymentDate()==null){
			message.setMessage("Date empty");
			message.setSeverity(MessageObject.WARN);
			message.setMessageCode("PAYMENT.DATE_W");
			return message;
		}
		
		return message;
	}
	private void addComponents() {
		JPanel ip1 = new JPanel(new RiverLayout(0,0));
		ip1.add("br p left",idLabel);
		ip1.add("tab hfill", idTextfield);
		ip1.add("br left", amountLabel);
		ip1.add("tab hfill", amountTextField);
		ip1.add("br left", dateLabel);
		ip1.add("tab hfill", dateChoser);
		container.add("br vfill hfill",ip1);
		add(container, BorderLayout.CENTER);
		add(toolBar,BorderLayout.NORTH);

	}

	@Override
	public void okPerformed(OkEvent event) {
		if(event.getObject() instanceof StockDocument){
			this.stockDocument = (StockDocument) event.getObject();
			inputState();
		}
		if(event.getObject() instanceof StockDocumentPayment){
			StockDocumentPayment stockDocumentPayment = (StockDocumentPayment) event.getObject();
			if(stockDocumentPayment.getID()!=null){
				idTextfield.setText(stockDocumentPayment.getID().toString());
			}
			amountTextField.setText(stockDocumentPayment.getAmount().toString());
			dateChoser.setDate(stockDocumentPayment.getPaymentDate());
			unbindData();
		}
	}
	public boolean removeValidate(){
		if(idTextfield.getText().equals("") || ((PaymentTableModel)paymentTable.getModel()).getStockDocumentsPayment().size()==0){
			return false;
		}
		return true;
	}
	public void inputState(){
		idTextfield.setText("");
		amountTextField.setText("");
		dateChoser.setDate(new Date());
		paymentTable.getSelectionModel().clearSelection();
		stockDocumentPayment.setID(null);
	}
	public void clearAll(){
		idTextfield.setText("");
		amountTextField.setText("");
		dateChoser.setDate(new Date());
		paymentTable.getSelectionModel().clearSelection();
		stockDocumentPayment.setID(null);
		getPaymentTable().getSelectionModel().clearSelection();
		stockDocument = null;
		((PaymentTableModel) getPaymentTable().getModel()).getStockDocumentsPayment().clear();
		getPaymentTable().updateUI();
	}

	public PaymentTable getPaymentTable() {
		return paymentTable;
	}
}
