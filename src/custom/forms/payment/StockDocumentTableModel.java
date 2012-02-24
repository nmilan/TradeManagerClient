package custom.forms.payment;

import hibernate.entityBeans.StockDocument;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import localization.Local;

public class StockDocumentTableModel extends AbstractTableModel{

	private List<Object> stockDocuments = new ArrayList<Object>();
	
	private String[] columnNames = {"STOCKDOCUMENT.DOCUMENTCODE", "STOCKDOCUMENT.CREATIONTIME", "STOCKDOCUMENTPAYMENT.PAYMENTDATE","STOCKDOCUMENT.SUMMARYINFO.TOTALPRICE","STOCKDOCUMENT.DEBT", "STOCKDOCUMENT.CANCELED", "STOCKDOCUMENT.PAID"};
	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		return stockDocuments!=null ? stockDocuments.size() : 0;
	}
	
	@Override
	public String getColumnName(int column) {
		return Local.getString(columnNames[column]);
	}

	@Override
	public Object getValueAt(int v, int k) {
		StockDocument item = ((StockDocument)stockDocuments.toArray()[v]);
		if(k==0){
			return item.getDocumentCode();	
		}
		if(k==1){
			return item.getCreationTime();
		}
		if(k==2){
			return item.getPaymentDay();
		}
		if(k==3){
			return item.getTotalPrice().setScale(2, BigDecimal.ROUND_HALF_UP);
		}
		if(k==4){
			return item.getDept().setScale(2, BigDecimal.ROUND_HALF_UP);
		}
		if(k==5){
			return item.getCanceled();
		}
		if(k==6){
			return item.getPaid();
		}
		return "Error";
	}
    
	public List<Object> getStockDocuments() {
		return stockDocuments;
	}

	public void setStockDocuments(List<Object> stockDocuments) {
		this.stockDocuments = stockDocuments;
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
