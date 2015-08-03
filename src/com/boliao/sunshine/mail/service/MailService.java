/**
 * 
 */
package com.boliao.sunshine.mail.service;

/**
 * @author liaobo
 * 
 */
public interface MailService {

	public boolean sendMailService(String subject, String content, String toEmail);

}
