/**
 * 
 */
package com.boliao.sunshine.mail;

/**
 * @author liaobo
 * 
 */

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * <p>
 * Title:用java发送邮件的例子
 * </p>
 * 
 * <p>
 * Description:发送图片附件并在html中使用该图片
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * 
 * @author liaobo
 * @blog http://blog.csdn.net/sunyujia/
 * @main sunyujia@yahoo.cn
 * @date Jun 10, 2008 12:35:26 AM
 */
public class SendHtmlMail {
	private static String username = "xxxx";
	private static String password = "xxxx";
	private static String smtpServer = "smtp.163.com";
	private static String fromMailAddress = "xxxx@163.com";
	private static String toMailAddress = "sunyujia@yahoo.cn";

	public static void main(String[] args) throws Exception {
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.host", smtpServer);
		// 获得邮件会话对象
		Session session = Session.getDefaultInstance(props, new SmtpAuthenticator(username, password));
		/** *************************************************** */
		// 创建MIME邮件对象
		MimeMessage mimeMessage = new MimeMessage(session);
		mimeMessage.setFrom(new InternetAddress(fromMailAddress));// 发件人
		mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(toMailAddress));// 收件人
		mimeMessage.setSubject("主题");
		mimeMessage.setSentDate(new Date());// 发送日期
		Multipart mp = new MimeMultipart("related");// related意味着可以发送html格式的邮件
		/** *************************************************** */
		BodyPart bodyPart = new MimeBodyPart();// 正文
		bodyPart.setDataHandler(new DataHandler("测<img src=/\"cid:IMG1/\" />试", "text/html;charset=GBK"));// 网页格式
		/** *************************************************** */
		BodyPart attachBodyPart = new MimeBodyPart();// 普通附件
		FileDataSource fds = new FileDataSource("c:/boot.ini");
		attachBodyPart.setDataHandler(new DataHandler(fds));
		// attachBodyPart.setFileName("=?GBK?B?" + new
		// sun.misc.BASE64Encoder().encode(fds.getName().getBytes()) + "?=");//
		// 解决附件名中文乱码
		mp.addBodyPart(attachBodyPart);
		/** *************************************************** */
		MimeBodyPart imgBodyPart = new MimeBodyPart(); // 附件图标
		byte[] bytes = readFile("C:/button.gif");
		ByteArrayDataSource fileds = new ByteArrayDataSource(bytes, "application/octet-stream");
		imgBodyPart.setDataHandler(new DataHandler(fileds));
		imgBodyPart.setFileName("button.gif");
		imgBodyPart.setHeader("Content-ID", "<img1></img1>");// 在html中使用该图片方法src="cid:IMG1"
		mp.addBodyPart(imgBodyPart);
		/** *************************************************** */
		mp.addBodyPart(bodyPart);
		mimeMessage.setContent(mp);// 设置邮件内容对象
		Transport.send(mimeMessage);// 发送邮件

	}

	/**
	 * 读取文件
	 * 
	 * @param file
	 *            文件路径
	 * @return 返回二进制数组
	 */
	public static byte[] readFile(String file) {
		FileInputStream fis = null;
		ByteArrayOutputStream bos = null;
		try {
			fis = new FileInputStream(file);
			bos = new ByteArrayOutputStream();
			int bytesRead;
			byte buffer[] = new byte[1024 * 1024];
			while ((bytesRead = fis.read(buffer)) != -1) {
				bos.write(buffer, 0, bytesRead);
				Arrays.fill(buffer, (byte) 0);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			try {
				if (bos != null)
					bos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return bos.toByteArray();
	}
}

/**
 * Smtp认证
 */
class SmtpAuthenticator extends Authenticator {
	String username = null;
	String password = null;

	// SMTP身份验证
	public SmtpAuthenticator(String username, String password) {
		this.username = username;
		this.password = password;
	}

	@Override
	public PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(this.username, this.password);
	}

}

class ByteArrayDataSource implements DataSource {

	private final String contentType;
	private final byte[] buf;
	private final int len;

	public ByteArrayDataSource(byte[] buf, String contentType) {
		this(buf, buf.length, contentType);
	}

	public ByteArrayDataSource(byte[] buf, int length, String contentType) {
		this.buf = buf;
		this.len = length;
		this.contentType = contentType;
	}

	public String getContentType() {
		if (contentType == null)
			return "application/octet-stream";
		return contentType;
	}

	public InputStream getInputStream() {
		return new ByteArrayInputStream(buf, 0, len);
	}

	public String getName() {
		return null;
	}

	public OutputStream getOutputStream() {
		throw new UnsupportedOperationException();
	}
}
