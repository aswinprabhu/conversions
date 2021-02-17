package com.wavefront.labs.convert.writer;

import com.wavefront.labs.convert.Utils.Tracker;
import com.wavefront.labs.convert.Writer;
import com.wavefront.rest.api.ApiClient;
import com.wavefront.rest.api.ApiException;
import com.wavefront.rest.api.client.AlertApi;
import com.wavefront.rest.api.client.DashboardApi;
import com.wavefront.rest.api.client.MaintenanceWindowApi;
import com.wavefront.rest.api.client.UserApi;
import com.wavefront.rest.models.Alert;
import com.wavefront.rest.models.Dashboard;
import com.wavefront.rest.models.MaintenanceWindow;
import com.wavefront.rest.models.TargetInfo;
import com.wavefront.rest.models.UserToCreate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class WavefrontPublisher implements Writer {

	private static final Logger logger = LogManager.getLogger(WavefrontPublisher.class);

	private Properties properties;
	private ApiClient apiClient;

	@Override
	public void init(Properties properties) {
		this.properties = properties;

		String url = properties.getProperty("convert.writer.publish.url");
		if (url.endsWith("/")) {
			url = url.substring(0, url.length() - 1);
		}

		if (url.endsWith("/api")) {
			url = url.substring(0, url.length() - 4);
		}

		String token = properties.getProperty("convert.writer.publish.token");

		apiClient = new ApiClient();
		apiClient.setBasePath(url);
		apiClient.setApiKey(token);
	}

	@Override
	public void writeDashboard(Dashboard dashboard) {
		DashboardApi dashboardApi = new DashboardApi(apiClient);

		boolean exists = false;
		try {
			dashboardApi.getDashboard(dashboard.getUrl());
			exists = true;
		} catch (ApiException e) {
			// do nothing, this indicates dashboard does not exists
		}

		try {
			if (exists) {
				dashboardApi.updateDashboard(dashboard.getUrl(), dashboard);
			} else {
				dashboardApi.createDashboard(dashboard);
			}
			Tracker.increment("WavefrontPublisher::writeDashboard Successful (Count)");
		} catch (ApiException e) {
			logger.error("API Exception creating dashboard: " + dashboard.getName(), e);
			Tracker.increment("WavefrontPublisher::writeDashboard Exception (Count)");
		}
	}

	@Override
	public void writeAlertTarget(TargetInfo alertTarget) {

	}

	@Override
	public void writeAlert(Alert alert) {

		AlertApi alertApi = new AlertApi(apiClient);
		boolean exists = false;

		try {
			alertApi.getAlert(alert.getId());
			exists = true;
		} catch (ApiException e) {
			logger.error("API Exception creating alert: " + alert.getName(), e);
		}

		try {
			if (exists) {
				alertApi.updateAlert(alert.getId(), alert);
			} else {
				alertApi.createAlert(alert);
			}
			Tracker.increment("WavefrontPublisher::writeAlert Successful (Count)");
		} catch (ApiException e) {
			logger.error("API Exception creating alert: " + alert.getName(), e);
			Tracker.increment("WavefrontPublisher::writeAlert Exception (Count)");
			System.out.println(new com.wavefront.rest.api.JSON().serialize(alert));
		}
	}

	@Override
	public void writeMaintenanceWindow(MaintenanceWindow maintenanceWindow) {
		try {
			new MaintenanceWindowApi(apiClient).createMaintenanceWindow(maintenanceWindow);
		} catch (ApiException e) {
			logger.error("API Exception creating maintenance window: " + maintenanceWindow.getTitle(), e);
		}

	}

	@Override
	public void writeUser(UserToCreate user) {
		boolean sendEmail = Boolean.getBoolean(properties.getProperty("convert.writer.publish.user.sendEmail", "false"));
		try {

			new UserApi(apiClient).createOrUpdateUser(user, sendEmail);
		} catch (ApiException e) {
			logger.error("API Exception creating user: " + user.getEmailAddress(), e);
		}

	}
}
