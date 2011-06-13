package org.code_house.mom.domain;

/**
 * Transfer of money to some client.
 * 
 * @author ldywicki
 */
public class Transfer {

	/**
	 * Unique transfer identifier.
	 */
	private String uuid;

	/**
	 * Client which will receive money.
	 */
	private Client destination;

	/**
	 * Money to transfer.
	 */
	private Money money;

	public Client getDestination() {
		return destination;
	}

	public void setDestination(Client destination) {
		this.destination = destination;
	}

	public Money getMoney() {
		return money;
	}

	public void setMoney(Money money) {
		this.money = money;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

}
