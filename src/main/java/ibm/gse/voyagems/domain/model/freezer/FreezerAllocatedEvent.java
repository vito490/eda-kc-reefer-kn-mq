package ibm.gse.voyagems.domain.model.freezer;


import ibm.gse.voyagems.domain.model.EventBase;

public class FreezerAllocatedEvent extends EventBase {

	private String orderID;
	private FreezerAllocatedPayload payload;

	public FreezerAllocatedEvent(long timestampMillis, String version, String orderID, FreezerAllocatedPayload payload) {
		this.timestampMillis = timestampMillis;
    	this.version = version;
    	this.type = EventBase.TYPE_CONTAINER_ALLOCATED;
		this.payload = payload;
		this.orderID = orderID;
	}

	public FreezerAllocatedEvent() {
		
	}
	
	public FreezerAllocatedPayload getPayload() {
		return payload;
	}

	public void setPayload(FreezerAllocatedPayload payload) {
		this.payload = payload;
	}

	public String getOrderId() {
		return orderID;
	}
	
}
