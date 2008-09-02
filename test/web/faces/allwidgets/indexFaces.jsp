<%@ page language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://metawidget.org/faces" prefix="m" %>

<f:view>

	<html>
		<body>

			<h:form id="form">

				<h:messages />

				<m:metawidget value="#{allWidgets}" style="aStyle" styleClass="aStyleClass" createHiddenFields="true" inspectorConfig="config/inspector-config.xml">
					<f:param name="tableStyle" value="aTableStyle"/>
					<f:param name="tableStyleClass" value="aTableStyleClass"/>
					<f:param name="columnClasses" value="aLabelClass, aComponentClass, aRequiredClass"/>
					<f:param name="instructionsStyle" value="aInstructionsStyle"/>
					<f:param name="instructionsStyleClass" value="aInstructionsStyleClass"/>
					<f:param name="buttonsStyle" value="aButtonsStyle"/>
					<f:param name="buttonsStyleClass" value="aButtonsStyleClass"/>
					
					<f:facet name="instructions">
						<h:outputText value="This page tests all possible widgets"/>
					</f:facet>
					
					<m:stub value="#{allWidgets.mystery}" attributes="required: false"/>

					<f:facet name="buttons">
						<h:commandButton value="Save" action="#{allWidgetsBean.save}"/>
					</f:facet>					
				</m:metawidget>

			</h:form>
		
		</body>	    
	</html>

</f:view>
