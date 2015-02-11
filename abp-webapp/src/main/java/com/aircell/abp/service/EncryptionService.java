package com.aircell.abp.service;

/**.
 *
 *
 *
 */
public interface EncryptionService {

    /**.
     * Takes a plain text string and encrypts and then Base 64 encodes it
     * @param plainText plain text to be encrypted
     * @return cipher text
     * encryption
     */
    public ServiceResponse<String> encryptString(String plainText);

    /**.
     * Takes a Base64 encoded and encrypted string and decrypts it
     * @param cipherText cipher text to be decrypted
     * @return plain text
     * decryption
     */
    public ServiceResponse<String> decryptString(String cipherText);

}
