package com.hatio.zest.layouts.elements;


import com.hatio.zest.layouts.interfaces.ConnectionLayout;
import com.hatio.zest.layouts.interfaces.NodeLayout;

public class CustomConnectionLayout implements ConnectionLayout {
	public CustomNodeLayout sourceNodeLayout;
	public CustomNodeLayout targetNodeLayout;
	private boolean visible;
	
	public CustomConnectionLayout(CustomNodeLayout sourceNodeLayout, CustomNodeLayout targetNodeLayout){
		this.sourceNodeLayout = sourceNodeLayout;
		this.targetNodeLayout = targetNodeLayout;
	}

	@Override
	public NodeLayout getSource() {
		return sourceNodeLayout;
	}

	@Override
	public NodeLayout getTarget() {
		return targetNodeLayout;
	}

	@Override
	public double getWeight() {
		// TODO Auto-generated method stub
		return 1.0;
	}

	@Override
	public boolean isDirected() {
		return false;
	}

	@Override
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	@Override
	public boolean isVisible() {
		return visible;
	}
}
