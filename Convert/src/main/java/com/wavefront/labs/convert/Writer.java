package com.wavefront.labs.convert;

import com.wavefront.rest.models.Alert;
import com.wavefront.rest.models.Dashboard;
import com.wavefront.rest.models.MaintenanceWindow;
import com.wavefront.rest.models.TargetInfo;
import com.wavefront.rest.models.UserToCreate;

import java.util.Properties;

public interface Writer {

	void init(Properties properties);

	void writeDashboard(Dashboard dashboard);

	void writeAlert(Alert alert);

	void writeAlertTarget(TargetInfo alertTarget);

	void writeMaintenanceWindow(MaintenanceWindow maintenanceWindow);

	void writeUser(UserToCreate user);
}
