package ibm.gse.voyagems.domain.model.freezer;


import ibm.gse.voyagems.domain.model.EventBase;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class FreezerNotFoundEvent extends EventBase {

	private FreezerNotFoundPayload payload;

	public FreezerNotFoundEvent(long timestampMillis, String version, String orderID, FreezerNotFoundPayload payload) {
		this.timestampMillis = timestampMillis;
    	this.version = version;
    	this.type = EventBase.TYPE_CONTAINER_NOT_FOUND;
		this.payload = payload;
	}

	public FreezerNotFoundEvent() {
		
	}
	
	
	public FreezerNotFoundPayload getPayload() {
		return payload;
	}

	public void setPayload(FreezerNotFoundPayload payload) {
		this.payload = payload;
	}
	

}
