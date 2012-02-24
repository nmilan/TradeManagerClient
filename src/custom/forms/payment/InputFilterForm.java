package custom.forms.payment;

import generic.MessagePanel;
import generic.components.GenericLabel;
import generic.events.LookupEvent;
import generic.form.FormStateEnum;
import generic.form.components.LookupTextInput;
import generic.listeners.LookupListener;
import generic.tools.MessageObject;
import hibernate.entityBeans.BusinessPartner;
import hibernate.entityBeans.StockDocument;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;

import actions.ActionManager;

import com.toedter.calendar.JDateChooser;

import custom.forms.payment.observer.OkEvent;
import custom.forms.payment.observer.OkListener;
import custom.forms.stockdocuments.StockDocumentInputForm;

import remotes.RemotesManager;
import util.DateUtil;
import util.ServerResponse;

import layouts.RiverLayout;
import localization.Local;
import model.metadata.EntityField;
import model.metadata.EntityMetadata;
import model.metadata.FieldTypeEnum;

public class InputFilterForm extends JPanel implements LookupListener,OkListener {
	private GenericLabel inBusinessPartnerIDLabel, inBusinessPartnerNameLabel;
	private JTextField inBusinessPartnerNameField;
	private JLabel dateFromLabel, ukupanDugLabel, ukupanDugVanValuteLabel;
	private JDateChooser dateFromTextField;
	private Map<String, JComponent> fieldMap = null;
	private EntityMetadata entity;
	private StockDocumentTable stockDocumentTable;
	private JScrollPane scrollPane;
	private LookupTextInput inBusinessPartnerLookup;
	private JPanel inputDirectionPanel = null;
	private JPanel mainPanel, dugPanel;
	private PaymentForm paymentForm;
	private JToolBar toolBar;
	private List<Object> stockDocuments;

	public InputFilterForm(PaymentForm paymentForm) {
		this.paymentForm = paymentForm;
		fieldMap = new HashMap<String, JComponent>();
		ServerResponse metaResponse = RemotesManager.getInstance()
				.getMetadataRemote().getEntityXml("StockDocument.xml");
		EntityMetadata em = new EntityMetadata((String) metaResponse.getData());
		entity = em;
		stockDocumentTable = new StockDocumentTable(paymentForm);
		paymentForm.addlistener(this);
		init();
		addComponents();
	}

	private void init() {
		toolBar = new JToolBar();
		toolBar.add(ActionManager.getInstance().getOkCustomAction());
		toolBar.add(ActionManager.getInstance().getCancelCustomAction());
		toolBar.setEnabled(false);

		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		inputDirectionPanel = new JPanel(new CardLayout(0, 0));

		inBusinessPartnerNameField = StockDocumentInputForm.getTextField(
				"String", false);

		inBusinessPartnerIDLabel = new GenericLabel(
				Local.getString("DOCUMENTSUBJECTCHOOSER.PARTNERID"));
		inBusinessPartnerIDLabel.setRequired(true);
		inBusinessPartnerNameLabel = new GenericLabel(
				Local.getString("DOCUMENTSUBJECTCHOOSER.PARTNERNAME"));
		inBusinessPartnerNameLabel.setRequired(true);

		inBusinessPartnerLookup = new LookupTextInput("BusinessPartner.xml",
				"ID", "Integer", null, "s5", this, false, null);
		dateFromTextField = paymentForm.getDateChooser();
		((JTextField)dateFromTextField.getComponent(1)).addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				searchStockDocument();
			}
		});
		dateFromLabel = new JLabel(Local.getString("PAYMENT.DATEFROM"));

		EntityField ef = entity.getFieldByName("BusinessPartnerID");
		fieldMap.put(ef.getFieldName(),
				(JComponent) inBusinessPartnerLookup.getComponent(0));
		ef = entity.getFieldByName("CreationTime");
		fieldMap.put(ef.getFieldName(),
				(JComponent) dateFromTextField.getComponent(1));
		scrollPane = new JScrollPane(stockDocumentTable);
		scrollPane.setPreferredSize(new Dimension(700,400));
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createTitledBorder(Local
				.getString("BUSINESSPARTNER")));

		JPanel ip1 = new JPanel(new RiverLayout(0, 0));
		ip1.add("p left", inBusinessPartnerIDLabel);
		ip1.add("tab hfill", inBusinessPartnerLookup);
		ip1.add("br left", inBusinessPartnerNameLabel);
		ip1.add("tab hfill", inBusinessPartnerNameField);
		ip1.add("p left", dateFromLabel);
		ip1.add("tab hfill", dateFromTextField);
		// ip1.add("br center", okButton);
		dugPanel = new JPanel();
		ukupanDugLabel = new JLabel("Ukupan dug: ");
		ukupanDugVanValuteLabel = new JLabel("Ukupan dug van valute: ");
		dugPanel.setLayout(new FlowLayout());
		dugPanel.add(ukupanDugLabel);
		dugPanel.add(ukupanDugVanValuteLabel);
		inputDirectionPanel.add(ip1, "Business");
	}

	private void addComponents() {
		JPanel panel = new JPanel();
		panel.setLayout(new RiverLayout());
		panel.add("br left hfill", inputDirectionPanel);
		panel.add("br vfill hfill", scrollPane);
		add(panel, BorderLayout.CENTER);
		add(toolBar, BorderLayout.NORTH);
		add(dugPanel, BorderLayout.SOUTH);

	}

	public JLabel getDateFromLabel() {
		return dateFromLabel;
	}

	public void setDateFromLabel(JLabel dateFromLabel) {
		this.dateFromLabel = dateFromLabel;
	}

	public void searchStockDocument() {
		if (!inBusinessPartnerNameField.getText().isEmpty()) {
			validateInput();
			ServerResponse response = getEntity();
			MessagePanel messPanel = new MessagePanel(Local.getString(response
					.getResponseCode()), response.getResponseMessage());
			if (response.getSeverity() == ServerResponse.INFO) {
				StockDocumentTableModel model = (StockDocumentTableModel) stockDocumentTable
						.getModel();
				this.stockDocuments = (List<Object>) response.getData();
				model.setStockDocuments((List<Object>) response.getData());
				stockDocumentTable.updateUI();
				setDugLabel((List<Object>) response.getData());
			} else {
				JOptionPane.showMessageDialog(this, messPanel,
						Local.getString("ERROR"), JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	public void clear() {
		inBusinessPartnerNameField.setText("");
		((JTextField) dateFromTextField.getComponent(1)).setText("");
		((JTextField) inBusinessPartnerLookup.getComponent(0)).setText("");
		StockDocumentTableModel model = (StockDocumentTableModel) stockDocumentTable
				.getModel();
		model.setStockDocuments(new ArrayList<Object>());
		stockDocumentTable.updateUI();
		ukupanDugLabel.setText("Ukupan dug: ");
		ukupanDugVanValuteLabel.setText("Ukupan dug van valute: ");

	}
	public void setDugLabel(List<Object> objects){
		ukupanDugLabel.setText("Ukupan dug: "
				+ getUkupanDug(objects));
		ukupanDugVanValuteLabel.setText("Ukupan dug van valute: "
				+ getUkupanDugVanValute(objects));
	}
	public BigDecimal getUkupanDug(List<Object> objects) {
		BigDecimal dug = new BigDecimal("0.00");
		dug.setScale(2, BigDecimal.ROUND_CEILING);
		for (Object o : objects) {
			StockDocument stDoc = (StockDocument) o;
			if (!stDoc.getPaid() &&!stDoc.getCanceled()) {
				dug = dug.add(stDoc.getDept());
			}
		}
		
		return dug.setScale(2, BigDecimal.ROUND_HALF_UP);
	}

	public BigDecimal getUkupanDugVanValute(List<Object> objects) {
		BigDecimal dug = new BigDecimal("0.00");
		dug.setScale(2, BigDecimal.ROUND_CEILING);
		for (Object o : objects) {
			StockDocument stDoc = (StockDocument) o;
			if (!stDoc.getPaid()&&!stDoc.getCanceled()) {
				if (stDoc.getPaymentDay().before(new Date())) {
					dug = dug.add(stDoc.getDept());
				}
			}
		}
		return dug.setScale(2, BigDecimal.ROUND_HALF_UP);
	}

	public ServerResponse getEntity() {
		Map<String, Object> searchMap = new HashMap<String, Object>();

		for (String k : fieldMap.keySet()) {
			EntityField ef = entity.getFieldByName(k);
			String text = "";
			if (ef.getFieldType().equals(FieldTypeEnum.BOOLEAN)) {
				text = ((JCheckBox) fieldMap.get(k)).isSelected() ? "true"
						: "false";
			} else
				text = ((JTextField) fieldMap.get(k)).getText();
			if (text.length() > 0) {
				if (ef.getFieldType().equals(FieldTypeEnum.DATE)) {
					text = ">" + ((JTextField) fieldMap.get(k)).getText();
				}
				searchMap.put(ef.getFieldName(), text);
			}
		}
		searchMap.put("Canceled", "false");
		ServerResponse response = RemotesManager.getInstance()
				.getGenericPersistenceRemote()
				.searchSelect(entity, searchMap, 1000, "id", null);
		return response;
	}

	public MessageObject validateInput() {
		MessageObject message = new MessageObject();
		message.setSeverity(MessageObject.NONE);
		for (String k : fieldMap.keySet()) {
			EntityField ef = entity.getFieldByName(k);
			if (!ef.getFieldType().equals(FieldTypeEnum.BOOLEAN)) {
				String s = ((JTextField) fieldMap.get(k)).getText();
				if (s.length() > 0) {
					if (ef.getFieldType().equals(FieldTypeEnum.DATE)) {
						if (s.startsWith(">=") || s.startsWith("<=")) {
							s = s.substring(2);
						} else if (s.startsWith(">") || s.startsWith("<")
								|| s.startsWith("=")) {
							s = s.substring(1);
						}
						try {
							new SimpleDateFormat(DateUtil.DATE_FORMAT).parse(s);
						} catch (Exception e) {
							message.setMessage("Search form input is incorrect.");
							message.setSeverity(MessageObject.WARN);
							message.setMessageCode("GENERICSEARCH.INPUTDATA_W");
						}
					} else if (ef.getFieldType().equals(FieldTypeEnum.STRING)) {
						continue;
					} else {
						if (s.startsWith(">=") || s.startsWith("<=")) {
							s = s.substring(2);
						} else if (s.startsWith(">") || s.startsWith("<")
								|| s.startsWith("=")) {
							s = s.substring(1);
						}
						try {
							if (ef.getFieldType().equals(FieldTypeEnum.INTEGER)) {
								Integer.parseInt(s);
							} else if (ef.getFieldType().equals(
									FieldTypeEnum.DOUBLE)) {
								Double.parseDouble(s);
							}
						} catch (Exception e) {
							message.setMessage("Search form input is incorrect.");
							message.setSeverity(MessageObject.WARN);
							message.setMessageCode("GENERICSEARCH.INPUTDATA_W");
						}
					}
				}
			}
		}
		return message;
	}

	@Override
	public void lookupComplete(LookupEvent le) {
		if (le.getSiblindKey().equals("s5")) {
			BusinessPartner bp = (BusinessPartner) le.getData();
			inBusinessPartnerLookup.setText(Integer.toString(bp.getID()));
			inBusinessPartnerNameField.setText(bp.getName());
			PaymentInputForm inp = paymentForm.getPaymentInputForm();
			inp.clearAll();
			searchStockDocument();
			stockDocumentTable.getSelectionModel().clearSelection();
			
		}

	}

	public StockDocumentTable getStockDocumentTable() {
		return stockDocumentTable;
	}

	public void clearTxt() {
		((JTextField) inBusinessPartnerLookup.getComponent(0)).setText("");
		inBusinessPartnerNameField.setText("");
		((JTextField) dateFromTextField.getComponent(1)).setText("");
		
	}

	@Override
	public void okPerformed(OkEvent event) {
		
		if(event.getSource() instanceof PaymentInputForm){
			int i = stockDocumentTable.getSelectedRow();
			searchStockDocument();
			StockDocument s = (StockDocument) ((StockDocumentTableModel)stockDocumentTable.getModel()).getStockDocuments().get(i);
			event.setObject(s);
		}
		
	}
}
