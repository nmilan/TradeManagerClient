package custom.forms.payment;

import hibernate.entityBeans.StockDocumentPayment;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import localization.Local;

public class PaymentTableModel extends AbstractTableModel{

	private List<Object> stockDocumentsPayment = new ArrayList<Object>();
	
	private String[] columnNames = {"STOCKDOCUMENTPAYMENT.PAYMENTDATE", "STOCKDOCUMENTPAYMENT.AMOUNT"};
	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		return stockDocumentsPayment!=null ? stockDocumentsPayment.size() : 0;
	}
	
	@Override
	public String getColumnName(int column) {
		return Local.getString(columnNames[column]);
	}

	@Override
	public Object getValueAt(int v, int k) {
		StockDocumentPayment item = ((StockDocumentPayment)stockDocumentsPayment.toArray()[v]);
		if(k==0){
			return item.getPaymentDate();	
		}
		if(k==1){
			return item.getAmount();
		}
		return "Error";
	}

	public List<Object> getStockDocumentsPayment() {
		return stockDocumentsPayment;
	}

	public void setStockDocumentsPayment(List<Object> stockDocumentsPayment) {
		this.stockDocumentsPayment = stockDocumentsPayment;
	}

	public String[] getColumnNames() {
		return columnNames;
	}

	public void setColumnNames(String[] columnNames) {
		this.columnNames = columnNames;
	}
	public Class getColumnClass(int col) {
		if (getRowCount() == 0) {
			return Object.class;
		} else {
			Object cellValue = getValueAt(0, col);
			return cellValue.getClass();
		}

	}
}