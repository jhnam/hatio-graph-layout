<%@ page language="java" contentType="text; charset=utf-8"
    pageEncoding="utf-8"%>
<%@page import="java.util.Enumeration"%>
<%@page import="com.hatio.zest.layouts.service.LayoutService"%>
<%
	String json = request.getParameter("json");
	String result = new LayoutService().algorithmApplay(json);
	response.setHeader("Access-Control-Allow-Origin","*");
	out.println(result);
%>