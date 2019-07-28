package hu.hordosikrisztian.lrs.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import org.apache.commons.net.ftp.FTPClient;

import hu.hordosikrisztian.lrs.exception.FtpException;

public class FtpUtils {
	
	private static Logger logger = Logger.getLogger("hu.hordosikrisztian.lrs.util.FtpUtils");
	
	public static void connectAndUpload(String localFileName, String givenRemoteFileName) {
		FTPClient ftpClient = new FTPClient();
		
		try (InputStream in = ClassLoader.getSystemResourceAsStream(localFileName)) {
			ftpClient.connect("ftp.unaux.com");
			ftpClient.login("unaux_24251900", "46bl0ala");
			
			ftpClient.storeFile(givenRemoteFileName, in);
			
			ftpClient.logout();
			ftpClient.disconnect();
			
			logger.info("Upload successful.");
		} catch (IOException e) {
			throw new FtpException("File not found or FTP connection unsuccessful.", e);
		}
	}

}
