package com.hatio.zest.layouts.elements;

import java.util.ArrayList;
import java.util.HashSet;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.graph.Node;

import com.hatio.zest.layouts.context.CustomLayoutContext;
import com.hatio.zest.layouts.dataStructures.DisplayIndependentDimension;
import com.hatio.zest.layouts.dataStructures.DisplayIndependentPoint;
import com.hatio.zest.layouts.interfaces.ConnectionLayout;
import com.hatio.zest.layouts.interfaces.EntityLayout;
import com.hatio.zest.layouts.interfaces.NodeLayout;
import com.hatio.zest.layouts.interfaces.SubgraphLayout;

public class CustomNodeLayout implements NodeLayout {
	private DisplayIndependentPoint location;
	private DisplayIndependentDimension size;
	private boolean minimized = false;
	public double x = 0;
	public double y = 0;
	public double width = 0;
	public double height = 0;
	public String id;
	public CustomLayoutContext context;
	
	public CustomNodeLayout(String id, CustomLayoutContext context) {
		this.id = id;
		this.context = context;
	}

	@Override
	public DisplayIndependentPoint getLocation() {
		if (location != null) {
			location.x = x;
			location.y = y;
		} else {
			location = new DisplayIndependentPoint(x, y);
		}
		return new DisplayIndependentPoint(location);
	}

	@Override
	public void setLocation(double x, double y) {
		this.x = x;
		this.y = y;
		if (location != null) {
			location.x = x;
			location.y = y;
		} else {
			location = new DisplayIndependentPoint(x, y);
		}
	}

	@Override
	public DisplayIndependentDimension getSize() {
		if (size != null) {
			size.width = width;
			size.height = height;
		} else {
			size = new DisplayIndependentDimension(width, height);
		}
		return new DisplayIndependentDimension(size);
	}

	@Override
	public void setSize(double width, double height) {
		this.width = width;
		this.height = height;
		if (size != null) {
			size.width = width;
			size.height = height;
		} else {
			size = new DisplayIndependentDimension(width, height);
		}
	}

	@Override
	public double getPreferredAspectRatio() {
		return 0;
	}

	@Override
	public boolean isResizable() {
		return false;
	}

	@Override
	public boolean isMovable() {
		return true;
	}

	@Override
	public EntityLayout[] getSuccessingEntities() {
		if (isPruned()) {
			return new NodeLayout[0];
		}
		ArrayList result = new ArrayList();
		HashSet addedSubgraphs = new HashSet();
		NodeLayout[] successingNodes = getSuccessingNodes();
		for (int i = 0; i < successingNodes.length; i++) {
			if (!successingNodes[i].isPruned()) {
				result.add(successingNodes[i]);
			} else {
				SubgraphLayout successingSubgraph = successingNodes[i]
						.getSubgraph();
				if (successingSubgraph.isGraphEntity()
						&& !addedSubgraphs.contains(successingSubgraph)) {
					result.add(successingSubgraph);
					addedSubgraphs.add(successingSubgraph);
				}
			}
		}
		return (EntityLayout[]) result.toArray(new EntityLayout[result.size()]);
	}

	@Override
	public EntityLayout[] getPredecessingEntities() {
		if (isPruned()) {
			return new NodeLayout[0];
		}
		ArrayList result = new ArrayList();
		HashSet addedSubgraphs = new HashSet();
		NodeLayout[] predecessingNodes = getPredecessingNodes();
		for (int i = 0; i < predecessingNodes.length; i++) {
			if (!predecessingNodes[i].isPruned()) {
				result.add(predecessingNodes[i]);
			} else {
				SubgraphLayout predecessingSubgraph = predecessingNodes[i]
						.getSubgraph();
				if (predecessingSubgraph.isGraphEntity()
						&& !addedSubgraphs.contains(predecessingSubgraph)) {
					result.add(predecessingSubgraph);
					addedSubgraphs.add(predecessingSubgraph);
				}
			}
		}
		return (EntityLayout[]) result.toArray(new EntityLayout[result.size()]);
	}

//	@Override
//	public Item[] getItems() {
//		return null;
//	}

	@Override
	public boolean isPrunable() {
		return false;
	}

	@Override
	public boolean isPruned() {
		return false;
	}

	@Override
	public SubgraphLayout getSubgraph() {
		return null;
	}

	@Override
	public void prune(SubgraphLayout subgraph) {
	}

	@Override
	public NodeLayout[] getSuccessingNodes() {
		ConnectionLayout[] connections = getOutgoingConnections();
		NodeLayout[] result = new NodeLayout[connections.length];
		for (int i = 0; i < connections.length; i++) {
			result[i] = connections[i].getTarget();
			if (result[i] == this) {
				result[i] = connections[i].getSource();
			}
		}
		return result;
	}

	@Override
	public NodeLayout[] getPredecessingNodes() {
		ConnectionLayout[] connections = getIncomingConnections();
		NodeLayout[] result = new NodeLayout[connections.length];
		for (int i = 0; i < connections.length; i++) {
			result[i] = connections[i].getSource();
			if (result[i] == this) {
				result[i] = connections[i].getTarget();
			}
		}
		return result;
	}

	@Override
	public ConnectionLayout[] getIncomingConnections() {
		ArrayList result = new ArrayList();
		ConnectionLayout[] arr = context.getConnections();
		for (int i = 0; i < arr.length; i++) { 
			ConnectionLayout conn = arr[i];
			CustomNodeLayout nodeLayout = (CustomNodeLayout)conn.getTarget();
			if(nodeLayout.id == this.id){
				result.add(conn);
			}
		}
		return (ConnectionLayout[]) result.toArray(new ConnectionLayout[result.size()]);
	}

	@Override
	public ConnectionLayout[] getOutgoingConnections() {
		ArrayList result = new ArrayList();
		ConnectionLayout[] arr = context.getConnections();
		for (int i = 0; i < arr.length; i++) { 
			ConnectionLayout conn = arr[i];
			CustomNodeLayout nodeLayout = (CustomNodeLayout)conn.getSource();
			if(nodeLayout.id == this.id){
				result.add(conn);
			}
		}
		return (ConnectionLayout[]) result.toArray(new ConnectionLayout[result.size()]);
	}

	@Override
	public void setMinimized(boolean minimized) {
		this.minimized = minimized;
	}

	@Override
	public boolean isMinimized() {
		return minimized;
	}
}
