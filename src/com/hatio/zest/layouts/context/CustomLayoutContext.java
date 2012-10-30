package com.hatio.zest.layouts.context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.hatio.zest.layouts.LayoutAlgorithm;
import com.hatio.zest.layouts.dataStructures.DisplayIndependentRectangle;
import com.hatio.zest.layouts.interfaces.ConnectionLayout;
import com.hatio.zest.layouts.interfaces.ContextListener;
import com.hatio.zest.layouts.interfaces.EntityLayout;
import com.hatio.zest.layouts.interfaces.ExpandCollapseManager;
import com.hatio.zest.layouts.interfaces.GraphStructureListener;
import com.hatio.zest.layouts.interfaces.LayoutContext;
import com.hatio.zest.layouts.interfaces.LayoutListener;
import com.hatio.zest.layouts.interfaces.NodeLayout;
import com.hatio.zest.layouts.interfaces.PruningListener;
import com.hatio.zest.layouts.interfaces.SubgraphLayout;

public class CustomLayoutContext implements LayoutContext {
	private LayoutAlgorithm mainAlgorithm;
	private ExpandCollapseManager expandCollapseManager;
	private final List contextListeners = new ArrayList();
	private final List layoutListeners = new ArrayList();
	private final List graphStructureListeners = new ArrayList();
	private final List pruningListeners = new ArrayList();
	
	public List nodeArr = new ArrayList();
	public List connectionArr = new ArrayList();
	public double x = 0;
	public double y = 0;
	public double width = 600;
	public double height = 450;

	@Override
	public NodeLayout[] getNodes() {
		return (NodeLayout[]) nodeArr.toArray(new NodeLayout[nodeArr.size()]);
	}

	@Override
	public ConnectionLayout[] getConnections() {
		return (ConnectionLayout[]) connectionArr.toArray(new ConnectionLayout[connectionArr.size()]);
	}

	@Override
	public EntityLayout[] getEntities() {
		return (EntityLayout[]) nodeArr.toArray(new EntityLayout[nodeArr.size()]);
	}

	@Override
	public ConnectionLayout[] getConnections(EntityLayout source, EntityLayout target) {
		ArrayList result = new ArrayList();

		ArrayList sourcesList = new ArrayList();
		if (source instanceof NodeLayout) {
			sourcesList.add(source);
		}
		if (source instanceof SubgraphLayout) {
			sourcesList.addAll(Arrays.asList(((SubgraphLayout) source)
					.getNodes()));
		}

		HashSet targets = new HashSet();
		if (target instanceof NodeLayout) {
			targets.add(target);
		}
		if (target instanceof SubgraphLayout) {
			targets.addAll(Arrays.asList(((SubgraphLayout) target).getNodes()));
		}

		for (Iterator iterator = sourcesList.iterator(); iterator.hasNext();) {
			NodeLayout source2 = (NodeLayout) iterator.next();
			ConnectionLayout[] outgoingConnections = source2
					.getOutgoingConnections();
			for (int i = 0; i < outgoingConnections.length; i++) {
				ConnectionLayout connection = outgoingConnections[i];
				if ((connection.getSource() == source2 && targets
						.contains(connection.getTarget()))
						|| (connection.getTarget() == source2 && targets
								.contains(connection.getSource()))) {
					result.add(connection);
				}
			}

		}
		return (ConnectionLayout[]) result.toArray(new ConnectionLayout[result.size()]);
	}

	@Override
	public DisplayIndependentRectangle getBounds() {
		return new DisplayIndependentRectangle(x, y, width, height);
	}

	@Override
	public boolean isBoundsExpandable() {
		return false;
	}

	@Override
	public SubgraphLayout[] getSubgraphs() {
		return null;
	}

	@Override
	public SubgraphLayout createSubgraph(NodeLayout[] nodes) {
		return null;
	}

	@Override
	public boolean isPruningEnabled() {
		return expandCollapseManager != null;
	}

	@Override
	public boolean isBackgroundLayoutEnabled() {
		return true;
	}

	@Override
	public void setMainLayoutAlgorithm(LayoutAlgorithm algorithm) {
		mainAlgorithm = algorithm;
	}

	@Override
	public LayoutAlgorithm getMainLayoutAlgorithm() {
		return mainAlgorithm;
	}

	@Override
	public void setExpandCollapseManager(
			ExpandCollapseManager expandCollapseManager) {
		this.expandCollapseManager = expandCollapseManager;
		expandCollapseManager.initExpansion(this);

	}

	@Override
	public ExpandCollapseManager getExpandCollapseManager() {
		return expandCollapseManager;
	}

	@Override
	public void addLayoutListener(LayoutListener listener) {
		layoutListeners.add(listener);
	}

	@Override
	public void removeLayoutListener(LayoutListener listener) {
		layoutListeners.remove(listener);
	}

	@Override
	public void addGraphStructureListener(GraphStructureListener listener) {
		graphStructureListeners.add(listener);
	}

	@Override
	public void removeGraphStructureListener(GraphStructureListener listener) {
		graphStructureListeners.remove(listener);
	}

	@Override
	public void addContextListener(ContextListener listener) {
		contextListeners.add(listener);
	}

	@Override
	public void removeContextListener(ContextListener listener) {
		contextListeners.remove(listener);
	}

	@Override
	public void addPruningListener(PruningListener listener) {
		pruningListeners.add(listener);
	}

	@Override
	public void removePruningListener(PruningListener listener) {
		pruningListeners.remove(listener);
	}

	@Override
	public void flushChanges(boolean animationHint) {
	}
}
