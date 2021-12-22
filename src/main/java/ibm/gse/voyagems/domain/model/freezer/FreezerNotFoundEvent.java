package ibm.gse.voyagems.domain.model.freezer;


import ibm.gse.voyagems.domain.model.EventBase;

public class FreezerNotFoundEvent extends EventBase {

	private String orderID;
	private FreezerNotFoundPayload payload;

	public FreezerNotFoundEvent(long timestampMillis, String version, String orderID, FreezerNotFoundPayload payload) {
		this.timestampMillis = timestampMillis;
    	this.version = version;
    	this.type = EventBase.TYPE_CONTAINER_NOT_FOUND;
		this.payload = payload;
		this.orderID = orderID;
	}

	public FreezerNotFoundEvent() {
		
	}
	
	
	public FreezerNotFoundPayload getPayload() {
		return payload;
	}

	public void setPayload(FreezerNotFoundPayload payload) {
		this.payload = payload;
	}
	
	public String getOrderId() {
		return orderID;
	}
	
}
