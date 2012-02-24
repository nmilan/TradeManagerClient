package custom.forms.payment;

import hibernate.entityBeans.StockDocument;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import localization.Local;

public class WhoNotPayTableModel  extends AbstractTableModel{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<Object> stockDocuments=null;
	BigDecimal allDept= new BigDecimal("0.00");
	
	
	private String[] columnNames = {"STOCKDOCUMENT.DOCUMENTCODE", "BUSINESSPARTNER.NAME", "BUSINESSPARTNER.OWNERNAME","BUSINESSPARTNER.TELEPHONE","STOCKDOCUMENTPAYMENT.PAYMENTDATE","STOCKDOCUMENT.DEBT"};
	
	
	
	public WhoNotPayTableModel() {
		super();
	}
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
			return item.getBusinessPartner().getName();
		}
		if(k==2){
			return item.getBusinessPartner().getOwnerName();
		}
		if(k==3){
			return item.getBusinessPartner().getTelephone();
		}
		if(k==4){
			return item.getPaymentDay();
		}
		if(k==5){
			return item.getDept().setScale(2, BigDecimal.ROUND_HALF_UP);
		}
		return "Error";
	}

	public List<Object> getStockDocuments() {
		return stockDocuments;
	}

	public void setStockDocuments(List<Object> stockDocuments) {
		this.stockDocuments = stockDocuments;
		for(Object o : stockDocuments){
			StockDocument s = (StockDocument)o;
			allDept = allDept.add(s.getDept().setScale(2, BigDecimal.ROUND_HALF_UP));
		}
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
	public BigDecimal getAllDept() {
		return allDept;
	}
	
	
}
