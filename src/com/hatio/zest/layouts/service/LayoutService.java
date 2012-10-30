package com.hatio.zest.layouts.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hatio.zest.layouts.LayoutAlgorithm;
import com.hatio.zest.layouts.algorithms.BoxLayoutAlgorithm;
import com.hatio.zest.layouts.algorithms.CompositeLayoutAlgorithm;
import com.hatio.zest.layouts.algorithms.DirectedGraphLayoutAlgorithm;
import com.hatio.zest.layouts.algorithms.GridLayoutAlgorithm;
import com.hatio.zest.layouts.algorithms.HorizontalShiftAlgorithm;
import com.hatio.zest.layouts.algorithms.RadialLayoutAlgorithm;
import com.hatio.zest.layouts.algorithms.SpaceTreeLayoutAlgorithm;
import com.hatio.zest.layouts.algorithms.SpringLayoutAlgorithm;
import com.hatio.zest.layouts.algorithms.SugiyamaLayoutAlgorithm;
import com.hatio.zest.layouts.algorithms.TreeLayoutAlgorithm;
import com.hatio.zest.layouts.context.CustomLayoutContext;
import com.hatio.zest.layouts.elements.CustomConnectionLayout;
import com.hatio.zest.layouts.elements.CustomNodeLayout;
import com.hatio.zest.layouts.interfaces.NodeLayout;

public class LayoutService {
	public LayoutService(){
		
	}
	public String algorithmApplay(String json){
		CustomLayoutContext context = new CustomLayoutContext();
		CustomNodeLayout nodeLayout;
		HashMap nodeMap = new HashMap();
		CustomConnectionLayout connectionLayout;
		
		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(json);
		JsonObject object = element.getAsJsonObject();
		JsonElement graphElement = object.get("graphInfo");
		JsonObject graphObject = graphElement.getAsJsonObject();
		String algorithmType = graphObject.get("algorithmType").getAsString();
		context.width = Double.parseDouble(graphObject.get("width").getAsString());
		context.height = Double.parseDouble(graphObject.get("height").getAsString());
		JsonArray nodes = graphObject.get("nodes").getAsJsonArray();
		for(int i=0; i<nodes.size(); i++){
			JsonObject nodeObject = nodes.get(i).getAsJsonObject();
			nodeLayout = new CustomNodeLayout(nodeObject.get("id").getAsString(), context);
			nodeLayout.x = Double.parseDouble(nodeObject.get("x").getAsString());
			nodeLayout.y = Double.parseDouble(nodeObject.get("y").getAsString());
			nodeLayout.width = Double.parseDouble(nodeObject.get("width").getAsString());
			nodeLayout.height = Double.parseDouble(nodeObject.get("height").getAsString());
			context.nodeArr.add(nodeLayout);
			nodeMap.put(nodeLayout.id, nodeLayout);
		}
		JsonArray connects = graphObject.get("connects").getAsJsonArray();
		String sourceId = null;
		String targetId = null;
		for(int j=0; j<connects.size(); j++){
			JsonObject connectObject = connects.get(j).getAsJsonObject();
			sourceId = connectObject.get("sourceId").getAsString();
			targetId = connectObject.get("targetId").getAsString();
			connectionLayout = new CustomConnectionLayout((CustomNodeLayout)nodeMap.get(sourceId), 
														(CustomNodeLayout)nodeMap.get(targetId));
			context.connectionArr.add(connectionLayout);
		}
		
		LayoutAlgorithm algorithm = getFactory(algorithmType);
		if(algorithm==null){
			return "";
		}
		algorithm.setLayoutContext(context);
		algorithm.applyLayout(true);
		
		NodeLayout[] arr = context.getNodes();
		List innerNodeArr = new ArrayList();
		for (int i = 0; i < arr.length; i++) { 
			CustomNodeLayout node = (CustomNodeLayout)arr[i];
			InnerNode innerNode = new InnerNode();
			innerNode.setId(node.id);
			innerNode.setX(node.x);
			innerNode.setY(node.y);
			innerNode.setWidth(node.width);
			innerNode.setHeight(node.height);
			innerNodeArr.add(innerNode);
		}	
		return new Gson().toJson(innerNodeArr);
	}
	
	private LayoutAlgorithm getFactory(String algorithmType){
		if("BoxLayout".equals(algorithmType)){
			return new BoxLayoutAlgorithm();
		}else if("CompositeLayout".equals(algorithmType)){
			//return new CompositeLayoutAlgorithm();
			return null;
		}else if("DirectedGraphLayout".equals(algorithmType)){
			return new DirectedGraphLayoutAlgorithm();
		}else if("GridLayout".equals(algorithmType)){
			return new GridLayoutAlgorithm();
		}else if("HorizontalShift".equals(algorithmType)){
			return new HorizontalShiftAlgorithm();
		}else if("RadialLayout".equals(algorithmType)){
			return new RadialLayoutAlgorithm();
		}else if("SpaceTreeLayout".equals(algorithmType)){
			return new SpaceTreeLayoutAlgorithm();
		}else if("SpringLayout".equals(algorithmType)){
			return new SpringLayoutAlgorithm();
		}else if("SugiyamaLayout".equals(algorithmType)){
			return new SugiyamaLayoutAlgorithm();
		}else if("TreeLayout".equals(algorithmType)){
			return new TreeLayoutAlgorithm();
		}
		return null;
	}
	
	class InnerNode{
		private String id;
		private double x = 0;
		private double y = 0;
		private double width = 0;
		private double height = 0;
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public double getX() {
			return x;
		}
		public void setX(double x) {
			this.x = x;
		}
		public double getY() {
			return y;
		}
		public void setY(double y) {
			this.y = y;
		}
		public double getWidth() {
			return width;
		}
		public void setWidth(double width) {
			this.width = width;
		}
		public double getHeight() {
			return height;
		}
		public void setHeight(double height) {
			this.height = height;
		}
	}
}
