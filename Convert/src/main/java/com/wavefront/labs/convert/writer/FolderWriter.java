package com.wavefront.labs.convert.writer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wavefront.labs.convert.Utils.Tracker;
import com.wavefront.labs.convert.Writer;
import com.wavefront.labs.convert.Utils;
import com.wavefront.rest.models.Alert;
import com.wavefront.rest.models.Dashboard;
import com.wavefront.rest.models.MaintenanceWindow;
import com.wavefront.rest.models.TargetInfo;
import com.wavefront.rest.models.UserToCreate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class FolderWriter implements Writer {

	private static final Logger logger = LogManager.getLogger(FolderWriter.class);

	private String baseFolder;
	private Gson gson;

	@Override
	public void init(Properties properties) {
		baseFolder = properties.getProperty("convert.writer.folder", "converted/");
		if (!baseFolder.endsWith(File.separator)) {
			baseFolder += File.separator;
		}
		gson = new GsonBuilder().setPrettyPrinting().create();
	}

	@Override
	public void writeDashboard(Dashboard dashboard) {
		try {
			String json = gson.toJson(dashboard);

			String fileName = dashboard.getUrl() + ".json";

			File file = new File(baseFolder + "dashboards");
			file.mkdirs();

			Files.write(Paths.get(file.getAbsolutePath() + File.separator + fileName), json.getBytes());
			Tracker.increment("FolderWriter::writeDashboard Successful (Count)");
		} catch (IOException e) {
			logger.error("Error writing dashboard: " + dashboard.getName(), e);
			Tracker.increment("FolderWriter::writeDashboard Exception (Count)");
		}
	}

	@Override
	public void writeAlertTarget(TargetInfo alertTarget) {

	}

	@Override
	public void writeAlert(Alert alert) {
		try {
			String json = gson.toJson(alert);

			String fileName = Utils.sluggify(alert.getName()) + ".json";

			File file = new File(baseFolder + "alerts");
			file.mkdirs();

			Files.write(Paths.get(file.getAbsolutePath() + File.separator + fileName), json.getBytes());
			Tracker.increment("FolderWriter::writeAlert Successful (Count)");
		} catch (IOException e) {
			logger.error("Error writing alert: " + alert.getName(), e);
			Tracker.increment("FolderWriter::writeAlert Exception (Count)");
		}

	}

	@Override
	public void writeMaintenanceWindow(MaintenanceWindow maintenanceWindow) {
		try {
			String json = gson.toJson(maintenanceWindow);

			String fileName = Utils.sluggify(maintenanceWindow.getTitle());
			fileName += "--";
			fileName += maintenanceWindow.getStartTimeInSeconds();
			fileName += ".json";

			File file = new File(baseFolder + "maintenanceWindows");
			file.mkdirs();

			Files.write(Paths.get(file.getAbsolutePath() + File.separator + fileName), json.getBytes());
			Tracker.increment("FolderWriter::writeMaintenanceWindow Successful (Count)");
		} catch (IOException e) {
			logger.error("Error writing maintenance window: " + maintenanceWindow.getTitle(), e);
			Tracker.increment("FolderWriter::writeMaintenanceWindow Exception (Count)");
		}
	}

	@Override
	public void writeUser(UserToCreate user) {
		try {
			String json = gson.toJson(user);

			String fileName = Utils.sluggify(user.getEmailAddress()) + ".json";

			File file = new File(baseFolder + "users");
			file.mkdirs();

			Files.write(Paths.get(file.getAbsolutePath() + File.separator + fileName), json.getBytes());
			Tracker.increment("FolderWriter::writeUser Successful (Count)");
		} catch (IOException e) {
			logger.error("Error writing user: " + user.getEmailAddress(), e);
			Tracker.increment("FolderWriter::writeUser Exception (Count)");
		}

	}
}
