package ibm.gse.voyagems.domain.model.freezer;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class FreezerAllocatedPayload {
	 private String orderID;
	 private String containerID;
	 
	 public FreezerAllocatedPayload(String oid, String cid) {
		 this.orderID = oid;
		 this.containerID = cid;
	 }

	public String getOrderID() {
		return orderID;
	}

	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}

	public String getContainerID() {
		return containerID;
	}

	public void setContainerID(String containerID) {
		this.containerID = containerID;
	}
}
