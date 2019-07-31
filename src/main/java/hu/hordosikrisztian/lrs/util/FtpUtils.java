package hu.hordosikrisztian.lrs.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

import org.apache.commons.net.ftp.FTPClient;

import hu.hordosikrisztian.lrs.exception.FtpException;

public class FtpUtils {

	private static Logger logger = Logger.getLogger("hu.hordosikrisztian.lrs.util.FtpUtils");

	// Connects to an FTP server and uploads the JSON file containing the report.
	public static void connectAndUpload(String localFileName, String givenRemoteFileName, String ftpPropertiesFileName) {
		Properties ftpProps = loadFtpProperties(ftpPropertiesFileName);

		String host = ftpProps.getProperty("ftp.host");
		String user = ftpProps.getProperty("ftp.user");
		String password = ftpProps.getProperty("ftp.password");

		if (host == null || user == null || password == null) {
			throw new FtpException("Property or properties not found.");
		}

		FTPClient ftpClient = new FTPClient();

		try (InputStream in = ClassLoader.getSystemResourceAsStream(localFileName)) {
			ftpClient.connect(host);
			ftpClient.login(user, password);

			ftpClient.storeFile(givenRemoteFileName, in);

			ftpClient.logout();
			ftpClient.disconnect();

			logger.info("Upload successful.");
		} catch (IOException e) {
			throw new FtpException("IOException in FTPUtils: ", e);
		}
	}

	// Loads FTP-related properties.
	private static Properties loadFtpProperties(String propertiesFileName) {
		Properties ftpProps = new Properties();

		try (InputStream in = ClassLoader.getSystemResourceAsStream(propertiesFileName)) {
			ftpProps.load(in);
		} catch (IOException e) {
			throw new FtpException("Could not find properties file: ", e);
		}

		return ftpProps;
	}

}
