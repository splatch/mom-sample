package org.code_house.mom.domain;

/**
 * Representation of the Money transfered between accounts.
 * 
 * @author ldywicki
 */
public class Money {

	public enum Currency {
		GBP,
		EUR,
		USD,
		PLN
	}

	/**
	 * Amount to transfer.
	 */
	private Double amount;

	/**
	 * Currency to transfer.
	 */
	private Currency currency;

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	
}
