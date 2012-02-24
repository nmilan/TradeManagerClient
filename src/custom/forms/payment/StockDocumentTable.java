package custom.forms.payment;

import hibernate.entityBeans.StockDocument;

import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import custom.forms.payment.observer.OkEvent;
import custom.forms.payment.observer.OkListener;

public class StockDocumentTable extends JTable implements MouseListener, OkListener{
	private PaymentForm paymentForm;
	public StockDocumentTable(PaymentForm paymentForm) {
		super();
		this.paymentForm = paymentForm;
		setModel(new StockDocumentTableModel());
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		paymentForm.addlistener(this);
		getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				try {
					int i=getSelectedRow();
					StockDocument s = (StockDocument) ((StockDocumentTableModel)getModel()).getStockDocuments().get(i);
					OkEvent event = new OkEvent(this, s);
					getPaymentForm().fireOkPerformed(event);
				} catch (Exception e2) {
					// TODO: handle exception
				}
				
			}
		});
		addMouseListener(this);
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
		if(event.getSource() instanceof PaymentInputForm){
		int i=getSelectedRow();
//		StockDocument s = (StockDocument) ((StockDocumentTableModel)getModel()).getStockDocuments().get(i);
//		event.setObject(s);
		
		}
	}
	
}
