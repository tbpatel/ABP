package com.aircell.abp.service;

/**.
 *
 *
 */
public interface FtpService {

    /**.
     * Uploads file to a desctination
     * @param destination desctination where file is to be uploaded
     * @param filename name file to be uploaded
     * @param upload byte[] value
     * @return boolean value specifying whether file is successfully uploaded
     */
    boolean uploadFile(String destination, String filename, byte[] upload);

}
