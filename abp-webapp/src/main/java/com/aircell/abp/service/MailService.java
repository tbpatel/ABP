package com.aircell.abp.service;

/**.
 * Classes implementing this interface are expected to be able to send
 * point-to-point messages from somewhere to somewhere (like email).
 * @author jon.boydell
 */
public interface MailService {

    /**. Sends a message somewhere
     * @param from Sender of mail
     * @param subject Subject of mail
     * @param plainText plain text in mail
     * @param htmlText html text in mail
     * @param to receiver of mail
     * @return ServiceRespose object
     *  */
    ServiceResponse<String> sendMessage(
    String from, String subject, String plainText, String htmlText, String... to
    );

    /**.
     * Determines whether or not the email service is available
     * @return true if the service is, false otherwise
     */
    public ServiceResponse isServiceAvailable();

}
