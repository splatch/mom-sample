package org.code_house.mom.producer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.swing.JComboBox;
import javax.swing.JTextField;

import org.code_house.mom.domain.Client;
import org.code_house.mom.domain.Money;
import org.code_house.mom.domain.Transfer;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig.Feature;
import org.jdesktop.beansbinding.AbstractBindingListener;
import org.jdesktop.beansbinding.AutoBinding;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Binding;
import org.jdesktop.beansbinding.BindingListener;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.beansbinding.PropertyStateEvent;
import org.springframework.beans.factory.InitializingBean;

/**
 * Very simple desktop application which produces messages from Swing controls.
 * 
 * @author ldywicki
 */
@SuppressWarnings("all")
public class ProducerController implements InitializingBean, ActionListener {

	private ProducerView view;
	private Transfer model;
	private ObjectMapper mapper;
	private MessageSender sender;

	// test clients
	private List<Client> clients = Arrays.asList(
		new Client("Client 0", 0),
		new Client("Client 1", 1),
		new Client("Client 2", 2),
		new Client("Client 3", 3)
	);

	// listener to update message area
	private BindingListener listener = new AbstractBindingListener() {

		@Override
		public void targetChanged(Binding binding, PropertyStateEvent event) {
			updateUuid();
		}

		@Override
		public void synced(Binding binding) {
			try {
				view.getMessageArea().setText(mapper.writeValueAsString(binding.getSourceObject()));
			} catch (Exception e) {
				// ignore ?
			}
		}
	};

	public ProducerController(ProducerView view) {
		this.model = new Transfer();
		model.setMoney(new Money());
		this.view = view;

		for (Client c : clients) {
			view.getClientComboBox().addItem(c);
		}

		for (Money.Currency c : Money.Currency.values()) {
			view.getCurrencyComboBox().addItem(c);
		}
	}

	protected void updateUuid() {
		model.setUuid(UUID.randomUUID().toString());

		view.getStatusBar().setText("Transfer ID: " + model.getUuid());
	}

	public void afterPropertiesSet() throws Exception {
		// ok I know it is a work-around
		mapper.configure(Feature.INDENT_OUTPUT, true);

		bind();
	}

	private void sendMessage() throws Exception {
		Map<String, Object> headers = new HashMap<String, Object>();
		headers.put("destination", model.getDestination().getId());
		sender.sendMessage(mapper.writeValueAsString(model), headers);
	}

	private void bind() {
		BeanProperty<Transfer, Double> amountProperty = BeanProperty.create("money.amount");
		BeanProperty<Transfer, Money.Currency> currencyProperty = BeanProperty.create("money.currency");
		BeanProperty<Transfer, Client> clientProperty = BeanProperty.create("destination");

		BeanProperty<JTextField, String> textFieldTextProperty = BeanProperty.create("text");
		BeanProperty<JComboBox, Money.Currency> comboProperty = BeanProperty.create("selectedItem");
		BeanProperty<JComboBox, Client> clientComboProperty = BeanProperty.create("selectedItem");

		AutoBinding amountBind = Bindings.createAutoBinding(UpdateStrategy.READ_WRITE, model,
			amountProperty, view.getAmountField(), textFieldTextProperty);
		AutoBinding currencyBind = Bindings.createAutoBinding(UpdateStrategy.READ_WRITE, model,
			currencyProperty, view.getCurrencyComboBox(), comboProperty);
		AutoBinding clientBind = Bindings.createAutoBinding(UpdateStrategy.READ_WRITE, model,
			clientProperty, view.getClientComboBox(), clientComboProperty);

		amountBind.addBindingListener(listener);
		currencyBind.addBindingListener(listener);
		clientBind.addBindingListener(listener);

		amountBind.bind();
		currencyBind.bind();
		clientBind.bind();

		view.getSubmitButton().addActionListener(this);
	}

	public void actionPerformed(ActionEvent event) {
		if (model.getDestination() == null || model.getMoney().getAmount() == null
			|| model.getMoney().getCurrency() == null) {
			view.getStatusBar().setText("Complete all fields to send message");
			return;
		}

		try {
			sendMessage();
			view.getStatusBar().setText("Transfer " + model.getUuid() + " sent");

			updateUuid();
		} catch (Exception e) {
			view.getStatusBar().setText("Unable to send transfer. " + e.getMessage());
		}
	}

	public void setMessageSender(MessageSender sender) {
		this.sender = sender;
	}

	public void setMapper(ObjectMapper mapper) {
		this.mapper = mapper;
	}

	
}
