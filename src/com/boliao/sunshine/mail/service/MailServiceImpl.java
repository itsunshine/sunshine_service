/**
 * 
 */
package com.boliao.sunshine.mail.service;

import com.boliao.sunshine.mail.SendSimpleMail;

/**
 * @author liaobo
 * 
 */
public class MailServiceImpl implements MailService {

	private static SendSimpleMail sendSimpleMail = SendSimpleMail.getInstance();

	private static MailServiceImpl instance = new MailServiceImpl();

	private MailServiceImpl() {
	}

	public static MailServiceImpl getInstance() {
		if (instance == null) {
			synchronized (MailServiceImpl.class) {
				if (instance == null) {
					instance = new MailServiceImpl();
				}
			}
		}
		return instance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.boliao.sunshine.mail.service.MailService#sendMailService(java.lang
	 * .String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean sendMailService(String subject, String content, String toEmail) {
		return sendSimpleMail.send(subject, content, toEmail);
	}

}
