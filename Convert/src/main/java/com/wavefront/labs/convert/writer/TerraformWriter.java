package com.wavefront.labs.convert.writer;

import com.wavefront.labs.convert.Utils;
import com.wavefront.labs.convert.Writer;
import com.wavefront.labs.convert.converter.datadog.DatadogAlertConverter;
import com.wavefront.labs.convert.converter.datadog.models.DatadogAlert;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

public class TerraformWriter implements Writer {

    private static final Logger logger = LogManager.getLogger(FolderWriter.class);

    public static final Pattern EMAIL_NOTIFICATION_PATTERN = Pattern.compile("@[a-zA-Z0-9_.+-@]+");

    public static final Map<String, String> ALERT_TARGETS_MAP = new HashMap<>();

    private String baseFolder;
    @Override
    public void init(Properties properties) {
        baseFolder = properties.getProperty("convert.writer.folder", "converted/");
        if (!baseFolder.endsWith(File.separator)) {
            baseFolder += File.separator;
        }
    }

    @Override
    public void writeDashboard(Dashboard dashboard) {

    }

    @Override
    public void writeAlertTarget(TargetInfo alertTarget) {

    }

    @Override
    public void writeAlert(Alert alert) {
        for (DatadogAlert ddAlert : DatadogAlertConverter.alerts) {
            if (alert.getName().equals(ddAlert.getName())) {
                datadogAlert = ddAlert;
            }
        }
        try {
            String fileName = Utils.sluggify(alert.getName()) + ".tf";

            File file = new File(baseFolder + "alerts");
            file.mkdirs();

            Files.write(Paths.get(file.getAbsolutePath() + File.separator + fileName), getTerraformCode(alert).getBytes());
        } catch (IOException e) {
            logger.error("Error writing alert: " + alert.getName(), e);
        }
    }

    @Override
    public void writeMaintenanceWindow(MaintenanceWindow maintenanceWindow) {

    }

    @Override
    public void writeUser(UserToCreate user) {

    }

    private String getTerraformCode(Alert alert) {
        StringBuffer sb = new StringBuffer("");

        //terraformAddProvider(sb);
        terraformAddResource(sb, alert);

        return sb.toString();
    }

    private void terraformAddProvider(StringBuffer sb) {
        sb.append(
            "provider \"wavefront\" {\n" +
            "  address = \"cloudhealth.wavefront.com\"\n" +
            "  token = \"xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx\"\n" +
            "}\n"
        );
    }

    private void terraformAddResource(StringBuffer sb, Alert alert) {
        sb.append("resource \"wavefront_alert\" \"" + Utils.sluggify(alert.getName()).replaceAll("-", "_") + "\" {\n");
        sb.append("  name = \"" + alert.getName() + "\"\n");
        sb.append("  condition = \"" + alert.getCondition().replaceAll("\"", "\\\\\"") + "\"\n");
        sb.append("  display_expression = \"" + alert.getCondition().replaceAll("\"", "\\\\\"") + "\"\n");
        sb.append("  minutes = 5\n");
        sb.append("  resolve_after_minutes = 5\n");
        sb.append("  severity = \"WARN\"\n");
        sb.append("  tags = [\n");
        sb.append("    \"terraform\",\n");
        sb.append("    \"datadog-converted\"\n");
        sb.append("  ]\n");
        sb.append("}");
    }
}
