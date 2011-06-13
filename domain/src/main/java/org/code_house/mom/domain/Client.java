package org.code_house.mom.domain;

/**
 * Class which represents client data.
 * 
 * @author ldywicki
 */
public class Client {

	/**
	 * Client id, an unique identifier.
	 */
	private Long id;

	/**
	 * Name of the client, human-like.
	 */
	private String name;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	
}
