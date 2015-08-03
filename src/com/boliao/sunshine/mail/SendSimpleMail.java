/**
 * 
 */
package com.boliao.sunshine.mail;

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * @author liaobo
 * 
 */
public class SendSimpleMail {

	private static SendSimpleMail instance = new SendSimpleMail();

	private final static String FROM = "1420514271@qq.com";
	private final static String MAIL_ACCOUNT = "1420514271";
	private final static String MAIL_PASSWORD = "Lb+syj1986";

	private SendSimpleMail() {
	}

	public static SendSimpleMail getInstance() {
		if (instance == null) {
			synchronized (SendSimpleMail.class) {
				if (instance == null) {
					instance = new SendSimpleMail();
				}
			}

		}
		return instance;
	}

	public boolean send(String subject, String content, String toEmail) {
		try {
			String to[] = { toEmail };
			Properties p = new Properties();
			p.put("mail.smtp.auth", "true");
			p.put("mail.transport.protocol", "smtp");
			p.put("mail.smtp.host", "smtp.qq.com");
			p.put("mail.smtp.port", "25");
			// 建立会话
			Session session = Session.getInstance(p);
			Message msg = new MimeMessage(session); // 建立信息

			msg.setFrom(new InternetAddress(FROM)); // 发件人

			String toList = getMailList(to);
			InternetAddress[] iaToList = new InternetAddress().parse(toList);

			msg.setRecipients(Message.RecipientType.TO, iaToList); // 收件人

			msg.setSentDate(new Date()); // 发送日期
			msg.setContent(content, "text/html;charset=utf-8");
			msg.setSubject(subject); // 主题
			// msg.setText(content); // 内容
			// 邮件服务器进行验证
			Transport tran = session.getTransport("smtp");
			tran.connect("smtp.qq.com", MAIL_ACCOUNT, MAIL_PASSWORD);
			tran.sendMessage(msg, msg.getAllRecipients());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 将mail数组，转换成用“，”分割的数组
	 * 
	 * @param mailArray
	 * @return
	 */
	private String getMailList(String[] mailArray) {

		StringBuffer toList = new StringBuffer();
		int length = mailArray.length;
		if (mailArray != null && length < 2) {
			toList.append(mailArray[0]);
		} else {
			for (int i = 0; i < length; i++) {
				toList.append(mailArray[i]);
				if (i != (length - 1)) {
					toList.append(",");
				}

			}
		}
		return toList.toString();

	}

	public static void main(String[] args) {
		SendSimpleMail sendSimpleMail = new SendSimpleMail();
		sendSimpleMail.send("javamail标题", "javamail内容解析测试", "591301927@qq.com");
	}
}
