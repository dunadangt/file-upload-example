package com.bytesw.inventarios.fileuploadexample.service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

@Service
public class UploadFileService {

	Logger logger = LoggerFactory.getLogger(UploadFileService.class);
	
	@Value("${app.local.file.dir}")
	private String localFileDirectory;
	
	@Value("${app.remote.file.dir}")
	private String remoteFileDirectory;
	
	@Value("${app.sftp.username}")
	private String sftpUsername;
	
	@Value("${app.sftp.password}")
	private String sftpPassword;
	
	@Value("${app.sftp.hostname}")
	private String sftpHostname;
	
	@Value("${app.sftp.port}")
	private Integer sftpPort;
	
	
	public void uploadLocal(byte[] bytes) {
		String fileName = localFileDirectory + File.separator + "upload_" + System.currentTimeMillis();
		
		try (FileOutputStream stream = new FileOutputStream(fileName)) {
			stream.write(bytes);
		} catch (IOException e) {
			logger.error("Ha ocurrido un error grabando archivo", e);
		}
	}
	
	public void uploadRemote(byte[] bytes) {
		Session session = null;
	    Channel channel = null;
	    ChannelSftp channelSftp = null;

	    try {
	        JSch jsch = new JSch();
	        session = jsch.getSession(sftpUsername, sftpHostname, sftpPort);
	        session.setPassword(sftpPassword);
	        java.util.Properties config = new java.util.Properties();
	        config.put("StrictHostKeyChecking", "no");
	        session.setConfig(config);
	        session.connect();
	        logger.info("Host connected.");
	        channel = session.openChannel("sftp");
	        channel.connect();
	        logger.info("sftp channel opened and connected.");
	        channelSftp = (ChannelSftp) channel;
	        channelSftp.cd(remoteFileDirectory);
	        
	        String fileName = "upload_" + System.currentTimeMillis();
	        
	        File f = new File(fileName);
	        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
	        channelSftp.put(bis, f.getName());
	        logger.info("File transfered successfully to host.");
	    } catch (Exception ex) {
	        logger.error("Exception found while tranfer the response.", ex);
	    } finally {
	        channelSftp.exit();
	        logger.info("sftp Channel exited.");
	        channel.disconnect();
	        logger.info("Channel disconnected.");
	        session.disconnect();
	        logger.info("Host Session disconnected.");
	    }
	}
}
