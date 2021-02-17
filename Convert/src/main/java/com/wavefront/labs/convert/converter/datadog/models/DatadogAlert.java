package com.wavefront.labs.convert.converter.datadog.models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@JsonIgnoreProperties({"deleted", "matching_downtimes", "multi", "created", "created_at", "creator", "org_id", "modified", "overall_state_modified", "overall_state", "type", "restricted_roles"})
public class DatadogAlert {

	private String id;
	private String name;
	private String query;
	private String message, messageCritical = "", messageWarning = "", messageOk = "", messageUnknown = "", messageGeneral = "";
	private List<String> tags;
	private DatadogAlertOptions options;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getMessage() {
		return message;
	}

	public String getMessageForGeneral() { return messageGeneral; }

	public String getMessageForCritical() { return messageCritical; }

	public String getMessageForWarning() { return messageWarning; }

	public String getMessageForOk() { return messageOk; }

	public String getMessageForUnknown() { return messageUnknown; }

	public List<String> getNotificationsForGeneral() {
		List<String> notifications = new ArrayList<String>();
		Matcher matcher = Pattern.compile("@[a-zA-Z0-9_.+-@]+").matcher(message);
		while (matcher.find()) {
			if (matcher.group() != null) {
				notifications.add(matcher.group());
			}
		}
		return notifications;
	}

	public String getNotificationsForCritical() { return messageCritical; }

	public String getNotificationsForWarning() { return messageWarning; }

	public String getNotificationsForOk() { return messageOk; }

	public String getNotificationsForUnknown() { return messageUnknown; }

	public void setMessage(String message) {
		this.message = message;
		categorizeMessage();
	}

	private void categorizeMessage() {
		if (message.indexOf("{{#is_alert}}") >= 0 && message.indexOf("{{/is_alert}}") > 0) {
			messageCritical = message.substring(message.indexOf("{{#is_alert}}") + "{{#is_alert}}".length(), message.indexOf("{{/is_alert}}"));
			messageGeneral = message.substring(0, message.indexOf("{{#is_alert}}")) + message.substring((message.indexOf("{{/is_alert}}") + "{{/is_alert}}".length()), message.length());
		}
		if (message.indexOf("{{#is_warning}}") >= 0 && message.indexOf("{{/is_warning}}") > 0) {
			messageWarning = message.substring(message.indexOf("{{#is_warning}}") + "{{#is_warning}}".length(), message.indexOf("{{/is_warning}}"));
			messageGeneral = message.substring(0, message.indexOf("{{#is_warning}}")) + message.substring((message.indexOf("{{/is_warning}}") + "{{/is_warning}}".length()), message.length());
		}
		if (message.indexOf("{{#is_ok}}") >= 0 && message.indexOf("{{/is_ok}}") > 0) {
			messageWarning = message.substring(message.indexOf("{{#is_ok}}") + "{{#is_ok}}".length(), message.indexOf("{{/is_ok}}"));
			messageGeneral = message.substring(0, message.indexOf("{{#is_ok}}")) + message.substring((message.indexOf("{{/is_ok}}") + "{{/is_ok}}".length()), message.length());
		}
		if (message.indexOf("{{#is_unknown}}") >= 0 && message.indexOf("{{/is_unknown}}") > 0) {
			messageUnknown = message.substring(message.indexOf("{{#is_unknown}}") + "{{#is_unknown}}".length(), message.indexOf("{{/is_unknown}}"));
			messageGeneral = message.substring(0, message.indexOf("{{#is_unknown}}")) + message.substring((message.indexOf("{{/is_unknown}}") + "{{/is_unknown}}".length()), message.length());
		}
		if ("".equals(messageGeneral)) {
			messageGeneral = message;
		}
		getNotificationsForGeneral();
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public DatadogAlertOptions getOptions() { return options; }

	public void setOptions(DatadogAlertOptions options) { this.options = options; }
}
