package custom.forms.payment;

import hibernate.entityBeans.StockDocument;
import hibernate.entityBeans.StockDocumentPayment;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import custom.forms.payment.observer.OkEvent;
import custom.forms.payment.observer.OkListener;

public class PaymentTable extends JTable implements MouseListener,OkListener{
	private PaymentForm paymentForm;
	public PaymentTable(PaymentForm paymentForm) {
		super();
		this.paymentForm =paymentForm;
		setModel(new PaymentTableModel());
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				try {
					
				
				int i=getSelectedRow();
				StockDocumentPayment s = (StockDocumentPayment) ((PaymentTableModel)getModel()).getStockDocumentsPayment().get(i);
				OkEvent event = new OkEvent(this, s);
				getPaymentForm().fireOkPerformed(event);
				} catch (Exception e2) {
					// TODO: handle exception
				}
			}
		});
		addMouseListener(this);
		paymentForm.addlistener(this);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}

	public PaymentForm getPaymentForm() {
		return paymentForm;
	}

	public void setPaymentForm(PaymentForm paymentForm) {
		this.paymentForm = paymentForm;
	}

	@Override
	public void okPerformed(OkEvent event) {
		if(event.getObject() instanceof StockDocument){
		StockDocument stockDocument = (StockDocument) event.getObject();
		((PaymentTableModel)getModel()).setStockDocumentsPayment(new ArrayList<Object>(stockDocument.getPayments()));
		updateUI();
		}
	}
	
	
}
