package com.salesagents.dataaccess.repository.security;


import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Sha512HashAlgorithm implements HashAlgorithm{
    public Sha512HashAlgorithm(){
    }

    @Override
    public boolean authenticate(String password, String hashedPassword){
        return hashedPassword.equals(hash(password));
    }

    @Override
    public String hash(String password)  {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte [] inputBytes = new byte[0];
        try {
            inputBytes = password.getBytes("UTF-16LE");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] digest = md.digest(inputBytes);
        return Base64.getEncoder().encodeToString(digest);
    }
}
